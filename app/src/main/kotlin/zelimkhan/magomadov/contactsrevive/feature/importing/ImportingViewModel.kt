package zelimkhan.magomadov.contactsrevive.feature.importing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import zelimkhan.magomadov.contactsrevive.domain.contacts.ConvertContactsToVcardUseCase
import zelimkhan.magomadov.contactsrevive.domain.contacts.FileRepository
import zelimkhan.magomadov.contactsrevive.domain.contacts.model.ContactsFormat
import zelimkhan.magomadov.contactsrevive.domain.fileName.GetFileNameUseCase
import javax.inject.Inject

@HiltViewModel
class ImportingViewModel @Inject constructor(
    private val getFileNameUseCase: GetFileNameUseCase,
    private val fileRepository: FileRepository,
    private val convertContactsToVcardUseCase: ConvertContactsToVcardUseCase,
) : ViewModel() {
    private val _importingState = MutableStateFlow(ImportingState())
    val mainState = _importingState.asStateFlow()

    private var selectedFilePath = ""

    fun onFileConvert() {
        viewModelScope.launch {
            val contactsFormat = ContactsFormat.fromExtension(mainState.value.selectedFileName)
            val vcard = convertContactsToVcardUseCase(selectedFilePath, contactsFormat)
            val convertedFileName =
                "${mainState.value.selectedFileName.dropLastWhile { it != '.' }}vcf"
            val convertedFile = fileRepository.save(content = vcard, name = convertedFileName)
            _importingState.value = mainState.value.copy(
                selectedFileName = convertedFileName,
                isFileConverted = true,
                convertedFile = convertedFile
            )
        }
    }

    fun onFileSelected(path: String) {
        viewModelScope.launch {
            selectedFilePath = path
            _importingState.value = mainState.value.copy(
                selectedFileName = getFileNameUseCase(path),
                isFileSelected = true,
                isFileConverted = false
            )
        }
    }
}