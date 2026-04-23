package zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.Contact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.MergedContact
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.merge
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParserFactory
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase

class ImportingViewModel(
    private val fileRepository: FileRepository,
    private val convertToVcard: ConvertContactsToVcardUseCase,
    private val backupDao: BackupDao,
    private val parserFactory: ContactsParserFactory,
) : ViewModel() {

    private val _state = MutableStateFlow(ImportingState())
    val state = _state.asStateFlow()

    private val _events = Channel<ImportingEvent>()
    val events = _events.receiveAsFlow()

    // Raw contacts cached between steps without cluttering the UI state
    private var rawContacts: List<Contact> = emptyList()
    private var fileUri: String = ""

    // ── File selection ────────────────────────────────────────────────────────

    fun onFileSelected(name: String, uri: String) {
        val format = name.toFormat()
        if (format == null) {
            viewModelScope.launch { _events.send(ImportingEvent.UnsupportedFormat) }
            return
        }
        fileUri = uri
        _state.update { ImportingState(fileName = name, isLoading = true) }
        parseFile(format, uri)
    }

    private fun parseFile(format: ContactsFormat, uri: String) = viewModelScope.launch {
        runCatching {
            val content = fileRepository.read(uri)
            parserFactory.create(format).invoke(content)
        }.fold(
            onSuccess = { contacts ->
                rawContacts = contacts
                _state.update {
                    it.copy(
                        isLoading = false,
                        totalRaw = contacts.size,
                        mergeGroups = contacts.buildMergeGroups(),
                        duplicateGroups = contacts.buildDuplicateGroups(),
                        step = ImportStep.Options,
                    )
                }
            },
            onFailure = { e ->
                _state.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            },
        )
    }

    // ── Options step ──────────────────────────────────────────────────────────

    fun setMergeByName(enabled: Boolean) = _state.update { it.copy(mergeByName = enabled) }
    fun setRemoveDuplicates(enabled: Boolean) =
        _state.update { it.copy(removeDuplicates = enabled) }

    fun navigateToPreview() = _state.update { it.copy(step = ImportStep.Preview) }

    fun applyOptionsAndContinue() {
        val processed = rawContacts.applyOptions(_state.value)
        _state.update {
            it.copy(
                contacts = processed.map { c -> SelectableContact(c) },
                step = ImportStep.ContactsList,
            )
        }
    }

    // ── Navigation back ───────────────────────────────────────────────────────

    /** Returns true if the back press was consumed, false means the OS should handle it. */
    fun onBack(): Boolean {
        val prev = when (_state.value.step) {
            ImportStep.FilePicker -> return false
            ImportStep.Options -> ImportStep.FilePicker
            ImportStep.Preview -> ImportStep.Options
            ImportStep.ContactsList -> ImportStep.Options
            ImportStep.Result -> ImportStep.FilePicker
        }
        if (prev == ImportStep.FilePicker) {
            // Full reset when going back to file picker
            _state.value = ImportingState()
            rawContacts = emptyList()
            fileUri = ""
        } else {
            _state.update { it.copy(step = prev) }
        }
        return true
    }

    // ── Contacts list step ────────────────────────────────────────────────────

    fun toggleContact(item: SelectableContact) = _state.update { s ->
        s.copy(contacts = s.contacts.map { if (it == item) it.copy(isSelected = !it.isSelected) else it })
    }

    fun selectAll() = _state.update { s ->
        s.copy(contacts = s.contacts.map { it.copy(isSelected = true) })
    }

    fun deselectAll() = _state.update { s ->
        s.copy(contacts = s.contacts.map { it.copy(isSelected = false) })
    }

    // ── Import ────────────────────────────────────────────────────────────────

    fun import() = viewModelScope.launch {
        val selected = _state.value.contacts.filter { it.isSelected }
        if (selected.isEmpty()) return@launch

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        val flat = selected.flatMap { sc ->
            sc.contact.phoneNumbers.map { phone -> Contact(sc.contact.name, phone) }
        }

        runCatching {
            val vcard = convertToVcard(flat)
            val file = fileRepository.save(vcard, "contacts_${System.currentTimeMillis()}.vcf")
            backupDao.insert(
                BackupEntity(
                    name = _state.value.fileName,
                    date = System.currentTimeMillis(),
                    contactCount = selected.size,
                    contactsJson = Json.encodeToString(flat),
                )
            )
            Pair(file, buildSummary(selected))
        }.fold(
            onSuccess = { (file, summary) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        resultFile = file,
                        summary = summary,
                        step = ImportStep.Result
                    )
                }
            },
            onFailure = { e ->
                _state.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            },
        )
    }

    fun startOver() {
        _state.value = ImportingState()
        rawContacts = emptyList()
        fileUri = ""
    }

    fun dismissError() = _state.update { it.copy(errorMessage = null) }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun buildSummary(selected: List<SelectableContact>): ImportSummary {
        val s = _state.value
        return ImportSummary(
            imported = selected.size,
            mergedGroups = if (s.mergeByName) s.mergeGroups.size else 0,
            removedDuplicates = if (s.removeDuplicates) s.duplicateGroups.size else 0,
        )
    }

    private fun List<Contact>.applyOptions(s: ImportingState): List<MergedContact> {
        var list = this
        if (s.removeDuplicates) list = list.distinctBy { it.name.trim() to it.phoneNumber.trim() }
        return if (s.mergeByName) list.merge()
        else list.map { MergedContact(it.name, listOf(it.phoneNumber)) }
    }

    private fun List<Contact>.buildMergeGroups(): List<MergeGroup> =
        groupBy { it.name.trim() }
            .filter { (_, v) -> v.map { it.phoneNumber }.distinct().size > 1 }
            .map { (name, v) -> MergeGroup(name, v.map { it.phoneNumber }.distinct()) }

    private fun List<Contact>.buildDuplicateGroups(): List<DuplicateGroup> =
        groupBy { it.phoneNumber.trim() }
            .filter { (_, v) -> v.size > 1 }
            .map { (phone, v) -> DuplicateGroup(phone, v.map { it.name }.distinct()) }

    private fun String.toFormat(): ContactsFormat? = when (substringAfterLast('.').lowercase()) {
        "json" -> ContactsFormat.JSON
        "html" -> ContactsFormat.HTML
        else -> null
    }
}