package zelimkhan.magomadov.contactsrevive.feature.importing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import zelimkhan.magomadov.contactsrevive.core.database.dao.BackupDao
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat.HTML
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.model.ContactsFormat.JSON
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.parser.ContactsParser
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.repository.FileRepository
import zelimkhan.magomadov.contactsrevive.feature.importing.domain.usecase.ConvertContactsToVcardUseCase

sealed interface ImportingEvent {
    data object IncorrectFileFormat : ImportingEvent
    data class Error(val message: String) : ImportingEvent
}

class ImportingViewModel(
    private val fileRepository: FileRepository,
    private val convertContactsToVcardUseCase: ConvertContactsToVcardUseCase,
    private val backupDao: BackupDao,
) : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(ImportingState())
    val state = _state.asStateFlow()

    private val _event = Channel<ImportingEvent>()
    val event = _event.receiveAsFlow()

    private var selectedFilePath = ""

    fun onFileSelected(name: String, path: String) {
        val fileFormat = name.getFormat()

        if (fileFormat == null) {
            sendEvent(ImportingEvent.IncorrectFileFormat)
            return
        }

        selectedFilePath = path
        _state.value = _state.value.copy(
            selectedFileName = name,
            isFileSelected = true,
            isFileConverted = false,
            showContactsList = false,
            error = null
        )
        onFileConvert() // Автоматическая конвертация после выбора
    }

    fun onFileConvert() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val format = _state.value.selectedFileName.getFormat()
                    ?: throw IllegalArgumentException("Неверный формат файла")

                val content = fileRepository.read(selectedFilePath)
                val parser: ContactsParser = get(named(format.name))
                val contacts = parser(content)

                _state.value = _state.value.copy(
                    contacts = contacts.map { SelectableContact(it) },
                    isLoading = false,
                    isFileConverted = true,
                    showContactsList = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка при загрузке файла"
                )
                sendEvent(ImportingEvent.Error(e.message ?: "Ошибка"))
            }
        }
    }

    fun toggleContactSelection(selectableContact: SelectableContact) {
        val currentContacts = _state.value.contacts.toMutableList()
        val index = currentContacts.indexOf(selectableContact)
        if (index != -1) {
            currentContacts[index] =
                selectableContact.copy(isSelected = !selectableContact.isSelected)
            _state.value = _state.value.copy(contacts = currentContacts)
        }
    }

    fun onSaveContacts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val selectedContacts =
                    _state.value.contacts.filter { it.isSelected }.map { it.contact }
                if (selectedContacts.isEmpty()) return@launch

                val vcard = convertContactsToVcardUseCase(selectedContacts)
                val fileName = "imported_${System.currentTimeMillis()}.vcf"
                val convertedFile = fileRepository.save(content = vcard, name = fileName)

                // Save to local backup
                backupDao.insertBackup(
                    BackupEntity(
                        name = _state.value.selectedFileName,
                        date = System.currentTimeMillis(),
                        contactCount = selectedContacts.size,
                        contactsJson = Json.encodeToString(selectedContacts)
                    )
                )

                _state.value = _state.value.copy(
                    isLoading = false,
                    isFileConverted = true,
                    convertedFile = convertedFile,
                    showContactsList = false,
                    importResult = ImportResult(
                        totalSelected = selectedContacts.size,
                        successCount = selectedContacts.size, // Можно добавить более сложную логику проверки
                        fileName = fileName
                    )
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Ошибка при сохранении: ${e.message}"
                )
            }
        }
    }

    private fun String.getFormat(): ContactsFormat? {
        return when (takeLastWhile { it != '.' }.lowercase()) {
            "json" -> JSON
            "html" -> HTML
            else -> null
        }
    }

    private fun sendEvent(event: ImportingEvent) {
        viewModelScope.launch { _event.send(event) }
    }
}
