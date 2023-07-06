package zelimkhan.magomadov.telegramcontacts.presentation.convert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import zelimkhan.magomadov.telegramcontacts.domain.usecase.ConvertTelegramContactsJsonToVcardUseCase
import zelimkhan.magomadov.telegramcontacts.domain.usecase.GetFileNameUseCase
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ConvertViewModel @Inject constructor(
    private val getFileNameUseCase: GetFileNameUseCase,
    private val convertTelegramContactsJsonToVcardUseCase: ConvertTelegramContactsJsonToVcardUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ConvertViewState())
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow<ConvertViewEvent?>(null)
    val event = _event.asStateFlow()

    private var selectedFilePath: String? = null
    private var convertedFile: File? = null

    fun processIntent(intent: ConvertViewIntent) {
        viewModelScope.launch {
            when (intent) {
                ConvertViewIntent.ConvertFile -> convertFile()
                is ConvertViewIntent.FileSelected -> onFileSelected(intent.filePath)
                ConvertViewIntent.OpenFile -> openFile()
                ConvertViewIntent.SelectFile -> selectFile()
                ConvertViewIntent.SendFile -> sendFile()
                ConvertViewIntent.FileOpened -> noActionOnTheFile()
                ConvertViewIntent.FileSent -> noActionOnTheFile()
            }
        }
    }

    private fun selectFile() {
        _event.value = ConvertViewEvent.OpenFilePicker
    }

    private suspend fun onFileSelected(path: String) {
        _event.value = null
        selectedFilePath = path
        _state.update {
            it.copy(selectedFileName = getFileNameUseCase(path))
        }
    }

    private suspend fun convertFile() {
        selectedFilePath?.apply {
            convertedFile = convertTelegramContactsJsonToVcardUseCase(
                jsonFilePath = this,
                convertedFileName = "contacts.vcf"
            )
            _state.update {
                it.copy(convertedFileName = convertedFile!!.name)
            }
        }
    }

    private fun openFile() {
        convertedFile?.apply {
            _event.value = ConvertViewEvent.OpenFile(this)
        }
    }

    private fun sendFile() {
        convertedFile?.apply {
            _event.value = ConvertViewEvent.SendFile(this)
        }
    }

    private fun noActionOnTheFile() {
        _event.value = null
    }
}
