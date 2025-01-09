package zelimkhan.magomadov.telegramcontacts.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import dagger.hilt.android.AndroidEntryPoint
import zelimkhan.magomadov.telegramcontacts.ui.theme.TelegramContactsTheme
import java.io.File

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelegramContactsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel: MainViewModel by viewModels()
                    val mainState = viewModel.mainState.collectAsState()

                    MainContent(
                        modifier = Modifier.padding(innerPadding),
                        mainState = mainState.value,
                        onIntent = viewModel::onIntent
                    )
                }
            }
        }
    }
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    mainState: MainState = MainState(),
    onIntent: (MainViewIntent) -> Unit = {}
) {
    val context = LocalContext.current
    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> onIntent(MainViewIntent.FileSelected(uri.toString())) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 32.dp),
            text = mainState.selectedFileName.ifEmpty {
                "Выберите файл с экспортированными контактами в формате json"
            },
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { getContent.launch("application/json") }
        ) {
            Text(text = "Выбрать файл")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (mainState.isFileSelected && mainState.isFileConverted.not()) {
            Button(
                onClick = { onIntent(MainViewIntent.Convert) })
            {
                Text(text = "Конвертировать")
            }
        }

        if (mainState.isFileConverted) {
            Row {
                Button(onClick = {
                    actionOnTheFile(context, mainState.convertedFile!!, Intent.ACTION_VIEW)
                }) {
                    Text(text = "Добавить в контакты")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(onClick = {
                    actionOnTheFile(context, mainState.convertedFile!!, Intent.ACTION_SEND)
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TelegramContactsTheme {
        MainContent()
    }
}