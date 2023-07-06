package zelimkhan.magomadov.telegramcontacts.presentation.convert

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import zelimkhan.magomadov.telegramcontacts.R
import zelimkhan.magomadov.telegramcontacts.presentation.core.FilePicker
import zelimkhan.magomadov.telegramcontacts.presentation.view.MiddleEllipsisText
import java.io.File

@Composable
fun ConvertScreen(
    state: ConvertViewState,
    event: ConvertViewEvent?,
    processIntent: (ConvertViewIntent) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState())
            .padding(top = 16.dp)
    ) {
        FileToConvertSection(state.selectedFileName)
        Spacer(Modifier.height(40.dp))
        SelectFileButton { processIntent(ConvertViewIntent.SelectFile) }
        Spacer(Modifier.height(16.dp))
        ConvertFileButton { processIntent(ConvertViewIntent.ConvertFile) }
        Spacer(Modifier.height(40.dp))
        if (state.convertedFileName.isNotEmpty()) {
            ConvertedFileSection(
                convertedFileName = state.convertedFileName,
                openFile = { processIntent(ConvertViewIntent.OpenFile) },
                sendFile = { processIntent(ConvertViewIntent.SendFile) }
            )
        }

        when (event) {
            ConvertViewEvent.OpenFilePicker -> FilePicker(
                type = "application/json",
                onResult = { filePath -> processIntent(ConvertViewIntent.FileSelected(filePath)) }
            )

            is ConvertViewEvent.OpenFile -> {
                actionOnTheFile(LocalContext.current, event.file, Intent.ACTION_VIEW)
                processIntent(ConvertViewIntent.FileOpened)
            }

            is ConvertViewEvent.SendFile -> {
                actionOnTheFile(LocalContext.current, event.file, Intent.ACTION_SEND)
                processIntent(ConvertViewIntent.FileOpened)
            }

            null -> {}
        }
    }
}

@Composable
private fun FileToConvertSection(selectedFileName: String) {
    Row {
        Text(
            text = stringResource(R.string.file_to_convert),
            fontSize = 18.sp
        )
        MiddleEllipsisText(
            text = selectedFileName,
            Modifier.padding(start = 8.dp),
            fontSize = 18.sp
        )
    }
}

@Composable
private fun SelectFileButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(R.string.select_file))
    }
}

@Composable
private fun ConvertFileButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = stringResource(R.string.convert_file))
    }
}

@Composable
private fun ConvertedFileSection(
    convertedFileName: String,
    openFile: () -> Unit,
    sendFile: () -> Unit
) {
    Box {
        var showMenu by remember { mutableStateOf(false) }

        OutlinedButton(
            onClick = { showMenu = true },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Gray
            )
        ) {
            Text(
                text = convertedFileName,
                color = Color.White
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    sendFile()
                },
                text = { Text(text = "Отправить файл") }
            )
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    openFile()
                },
                text = { Text(text = "Открыть файл") }
            )
        }
    }
}

private fun actionOnTheFile(context: Context, file: File, action: String) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.TelegramContactsFileProvider",
        file
    )
    val intent = Intent().apply {
        this.action = action
        putExtra(Intent.EXTRA_TEXT, file)
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "text/x-vcard"
    }
    //val chooser = Intent.createChooser(shareIntent, "")
    context.startActivity(intent)
}
