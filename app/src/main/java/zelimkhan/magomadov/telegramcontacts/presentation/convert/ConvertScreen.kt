package zelimkhan.magomadov.telegramcontacts.presentation.convert

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        FileToConvertSection(state.selectedFileName)
        Spacer(Modifier.height(40.dp))
        SelectFileButton { processIntent(ConvertViewIntent.SelectFile) }
        Spacer(Modifier.height(16.dp))
        ConvertFileButton { processIntent(ConvertViewIntent.ConvertFile) }
        Spacer(Modifier.height(40.dp))
        ConvertedFileSection(state.convertedFileName) { processIntent(ConvertViewIntent.OpenFile) }

        when (event) {
            ConvertViewEvent.OpenFilePicker -> FilePicker(
                type = "application/json",
                onResult = { filePath -> processIntent(ConvertViewIntent.FileSelected(filePath)) }
            )

            is ConvertViewEvent.OpenFile -> {
                openFile(LocalContext.current, event.file)
                processIntent(ConvertViewIntent.FileOpened)
            }

            null -> {}
        }
    }
}

fun openFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.TelegramContactsFileProvider",
        file
    )
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, file)
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "text/x-vcard"
    }
    val chooser = Intent.createChooser(shareIntent, context.getString(R.string.open))
    context.startActivity(chooser)
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
private fun ConvertedFileSection(convertedFileName: String, onClick: () -> Unit) {
    if (convertedFileName.isEmpty()) return
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Gray
        )
    ) {
        Text(
            text = convertedFileName,
            color = Color.White
        )
    }
}
