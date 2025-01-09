package zelimkhan.magomadov.telegramcontacts.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import zelimkhan.magomadov.telegramcontacts.domain.repository.FileRepository
import zelimkhan.magomadov.telegramcontacts.domain.usecase.ConvertJsonToVcardUseCase
import zelimkhan.magomadov.telegramcontacts.domain.usecase.GetFileNameUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getFileNameUseCase: GetFileNameUseCase,
    private val fileRepository: FileRepository,
    private val convertJsonToVcardUseCase: ConvertJsonToVcardUseCase
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private var selectedFilePath = ""

    fun onIntent(intent: MainViewIntent) {
        viewModelScope.launch {
            when (intent) {
                MainViewIntent.Convert -> onFileConvert()
                is MainViewIntent.FileSelected -> onFileSelected(intent.filePath)
            }
        }
    }

    private suspend fun onFileConvert() {
        val vcard = convertJsonToVcardUseCase(selectedFilePath)
        val convertedFileName = "${mainState.value.selectedFileName.dropLastWhile { it != '.' }}vcf"
        val convertedFile = fileRepository.save(content = vcard, name = convertedFileName)
        _mainState.value = mainState.value.copy(
            selectedFileName = convertedFileName,
            isFileConverted = true,
            convertedFile = convertedFile
        )
    }

    private suspend fun onFileSelected(filePath: String) {
        selectedFilePath = filePath
        _mainState.value = mainState.value.copy(
            selectedFileName = getFileNameUseCase(filePath),
            isFileSelected = true,
            isFileConverted = false
        )
    }
}