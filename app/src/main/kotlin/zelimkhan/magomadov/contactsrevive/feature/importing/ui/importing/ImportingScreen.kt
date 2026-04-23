package zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.changes.ChangesPreviewScreen
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.contacts.ContactsListScreen
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.filePicker.FilePickerScreen
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.processing.ProcessingOptionsScreen
import zelimkhan.magomadov.contactsrevive.ui.util.ObserveAsEvents
import java.io.File

@Composable
fun ImportingScreen(viewModel: ImportingViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Back press consumed by ViewModel step machine
    BackHandler(enabled = state.step != ImportStep.FilePicker) { viewModel.onBack() }

    ObserveAsEvents(viewModel.events) { /* UnsupportedFormat is already reflected in errorMessage */ }

    val fileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { viewModel.onFileSelected(it.fileName(context), it.toString()) }
        }

    // Crossfade: instant-feeling, no slide jank, just a clean 160ms fade between steps
    Crossfade(
        targetState = state.step,
        animationSpec = tween(160),
        label = "import_step",
    ) { step ->
        when (step) {
            ImportStep.FilePicker -> FilePickerScreen(
                state = state,
                onPickFile = { fileLauncher.launch("*/*") },
            )

            ImportStep.Options -> ProcessingOptionsScreen(
                state = state,
                onMergeToggle = viewModel::setMergeByName,
                onDedupToggle = viewModel::setRemoveDuplicates,
                onShowPreview = viewModel::navigateToPreview,
                onContinue = viewModel::applyOptionsAndContinue,
            )

            ImportStep.Preview -> ChangesPreviewScreen(
                state = state,
                onApply = viewModel::applyOptionsAndContinue,
            )

            ImportStep.ContactsList -> ContactsListScreen(
                state = state,
                onToggle = viewModel::toggleContact,
                onSelectAll = viewModel::selectAll,
                onDeselectAll = viewModel::deselectAll,
                onImport = viewModel::import,
            )

            ImportStep.Result -> ImportResultScreen(
                state = state,
                onAddToPhone = {
                    state.resultFile?.let {
                        openVcf(
                            context,
                            it,
                            Intent.ACTION_VIEW
                        )
                    }
                },
                onShare = { state.resultFile?.let { openVcf(context, it, Intent.ACTION_SEND) } },
                onStartOver = viewModel::startOver,
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun openVcf(context: Context, file: File, action: String) {
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(action).apply {
        when (action) {
            Intent.ACTION_VIEW -> {
                setDataAndType(uri, "text/x-vcard"); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            Intent.ACTION_SEND -> {
                putExtra(Intent.EXTRA_STREAM, uri); type =
                    "text/x-vcard"; addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }
    context.startActivity(Intent.createChooser(intent, null))
}

@SuppressLint("Range")
private fun Uri.fileName(context: Context): String =
    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)) else null
    } ?: "Unknown"