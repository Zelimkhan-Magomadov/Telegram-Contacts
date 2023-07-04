package zelimkhan.magomadov.telegramcontacts.presentation.core

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun FilePicker(type: String, onResult: (String) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { onResult(it.toString()) } }

    LaunchedEffect(Unit) {
        launcher.launch(type)
    }
}
