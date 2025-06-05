package zelimkhan.magomadov.contactsrevive.feature.importing

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import zelimkhan.magomadov.contactsrevive.ui.theme.ContactsReviveTheme
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream65
import java.io.File

@Composable
fun ImportingScreen(
    viewModel: ImportingViewModel = hiltViewModel(),
) {
    val mainState = viewModel.mainState.collectAsStateWithLifecycle()

    NotFileSelectedContent(
        modifier = Modifier,
        importingState = mainState.value,
        onFileSelected = viewModel::onFileSelected,
        onFileConvert = viewModel::onFileConvert
    )
}

@Composable
fun NotFileSelectedContent(
    modifier: Modifier = Modifier,
    importingState: ImportingState,
    onFileSelected: (path: String) -> Unit,
    onFileConvert: () -> Unit,
) {
    val context = LocalContext.current
    rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> onFileSelected(uri.toString()) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = importingState.selectedFileName.ifEmpty {
                "Выберите файл с экспортированными контактами из Telegram"
            },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(133.dp))

        Button(
            modifier = Modifier
                .width(236.dp)
                .height(48.dp),
            onClick = {
                val url = "https://yoomoney.ru/to/4100119133698213"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }
        ) {
            Text(text = "Выбрать файл")
        }

        Spacer(modifier.height(32.dp))

        Text(
            text = "Поддерживаемые типы файлов: \nJSON и HTML",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            color = Cream65
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (importingState.isFileSelected && importingState.isFileConverted.not()) {
            Button(
                onClick = onFileConvert
            )
            {
                Text(text = "Конвертировать")
            }
        }

        if (importingState.isFileConverted) {
            Row {
                Button(onClick = {
                    actionOnTheFile(context, importingState.convertedFile!!, Intent.ACTION_VIEW)
                }) {
                    Text(text = "Добавить в контакты")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    actionOnTheFile(context, importingState.convertedFile!!, Intent.ACTION_SEND)
                }) {
                    Text(text = "Передать")
                }
            }
        }
    }
}

private fun actionOnTheFile(context: Context, file: File, action: String) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.FileProvider",
        file
    )
    val intent = Intent().apply {
        this.action = action
        when (action) {
            Intent.ACTION_VIEW -> {
                setDataAndType(uri, "text/x-vcard")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            Intent.ACTION_SEND -> {
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/x-vcard"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }
    val chooser = Intent.createChooser(intent, "Выберите приложение")
    context.startActivity(chooser)
}

@Preview(showBackground = true, backgroundColor = Color.BLACK.toLong())
@Composable
private fun Preview() {
    ContactsReviveTheme {
        NotFileSelectedContent(
            modifier = Modifier,
            importingState = ImportingState(),
            onFileSelected = {},
            onFileConvert = {}
        )
    }
}