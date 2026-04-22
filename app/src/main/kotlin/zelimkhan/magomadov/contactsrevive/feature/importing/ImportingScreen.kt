package zelimkhan.magomadov.contactsrevive.feature.importing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import zelimkhan.magomadov.contactsrevive.R
import zelimkhan.magomadov.contactsrevive.ui.theme.AccentColor
import zelimkhan.magomadov.contactsrevive.ui.theme.SuccessColor
import java.io.File

@Composable
fun ImportingScreen(
    viewModel: ImportingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.showContactsList) {
        ContactsScreen(
            state = state,
            onContactToggle = viewModel::toggleContactSelection,
            onSave = viewModel::onSaveContacts
        )
    } else {
        ImportingContent(
            state = state,
            onFileSelected = viewModel::onFileSelected,
            onFileConvert = viewModel::onFileConvert
        )
    }
}

@Composable
private fun ImportingContent(
    state: ImportingState,
    onFileSelected: (name: String, path: String) -> Unit,
    onFileConvert: () -> Unit,
) {
    val context = LocalContext.current
    val getFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            onFileSelected(uri.fileName(context), uri.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(AccentColor.copy(alpha = 0.5f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = AccentColor)
                } else {
                    Icon(
                        imageVector = if (state.isFileSelected && state.error == null) Icons.Default.Description else Icons.Default.FileUpload,
                        contentDescription = null,
                        tint = if (state.error != null) MaterialTheme.colorScheme.error else if (state.isFileSelected) SuccessColor else AccentColor,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = when {
                            state.error != null -> state.error
                            state.isFileSelected -> state.selectedFileName
                            else -> stringResource(R.string.importing_title)
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = if (state.error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { getFileLauncher.launch("*/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = !state.isLoading
        ) {
            Text(
                text = if (state.isFileSelected) "Выбрать другой файл" else stringResource(R.string.select_file),
                style = MaterialTheme.typography.labelLarge
            )
        }

        AnimatedVisibility(
            visible = state.isFileConverted && !state.showContactsList,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut()
        ) {
            if (state.isFileConverted && state.convertedFile != null) {
                ImportResultCard(state.importResult, context, state.convertedFile)
            }
        }

        if (!state.isFileSelected && state.error == null) {
            Text(
                text = stringResource(R.string.importing_subtitle),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@SuppressLint("Range")
fun Uri.fileName(context: Context): String {
    val cursor = context.contentResolver.query(this, null, null, null, null) ?: return "Unknown"
    cursor.use {
        if (it.moveToFirst()) {
            return it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return "Unknown"
}

@Composable
private fun ImportResultCard(
    result: ImportResult?,
    context: Context,
    file: File,
) {
    if (result == null) return

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(SuccessColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Импорт завершен",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = SuccessColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Все выбранные контакты успешно сконвертированы в VCF",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ResultStatItem(label = "Всего", value = "${result.totalSelected}")
                ResultStatItem(label = "Готово", value = "${result.successCount}")
                ResultStatItem(label = "Ошибки", value = "0")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { actionOnTheFile(context, file, Intent.ACTION_VIEW) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessColor)
            ) {
                Text("Добавить в телефон", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { actionOnTheFile(context, file, Intent.ACTION_SEND) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            ) {
                Text("Поделиться VCF", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun ResultStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = AccentColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

private fun actionOnTheFile(context: Context, file: File, action: String) {
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
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
    val chooser = Intent.createChooser(intent, "Выберите действие")
    context.startActivity(chooser)
}
