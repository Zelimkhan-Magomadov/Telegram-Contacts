package zelimkhan.magomadov.contactsrevive.feature.importing.ui.filePicker

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.R
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingState
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream20
import zelimkhan.magomadov.contactsrevive.ui.theme.Error
import zelimkhan.magomadov.contactsrevive.ui.theme.Error15
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60

@Composable
internal fun FilePickerScreen(
    state: ImportingState,
    onPickFile: () -> Unit,
) {
    val hasError = state.errorMessage != null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        FileIcon(isLoading = state.isLoading, hasError = hasError)

        Spacer(Modifier.height(28.dp))

        Text(
            text = when {
                hasError -> "Не удалось прочитать файл"
                state.isLoading -> "Читаем файл…"
                state.fileName.isNotEmpty() -> state.fileName
                else -> stringResource(R.string.importing_title)
            },
            style = MaterialTheme.typography.headlineMedium,
            color = if (hasError) Error else MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = when {
                hasError -> state.errorMessage.orEmpty()
                state.isLoading -> ""
                else -> stringResource(R.string.importing_subtitle)
            },
            style = MaterialTheme.typography.bodyMedium,
            color = if (hasError) Error.copy(alpha = .7f) else OnSurface60,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onPickFile,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream,
                contentColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = Cream20,
                disabledContentColor = Cream,
            ),
        ) {
            Text(
                text = if (state.fileName.isNotEmpty()) "Выбрать другой файл"
                else stringResource(R.string.select_file),
                style = MaterialTheme.typography.labelLarge,
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Поддерживаемые типы файлов:\nJSON и HTML",
            style = MaterialTheme.typography.bodySmall,
            color = OnSurface30,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FileIcon(isLoading: Boolean, hasError: Boolean) {
    val pulse by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "scale",
    )

    Box(
        modifier = Modifier
            .size(120.dp)
            .scale(if (isLoading) pulse else 1f)
            .clip(CircleShape)
            .background(if (hasError) Error15 else Cream10),
        contentAlignment = Alignment.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Cream,
                strokeWidth = 2.dp,
                modifier = Modifier.size(36.dp)
            )
        } else {
            Text(
                text = if (hasError) "✕" else "↑",
                fontSize = 36.sp,
                color = if (hasError) Error else Cream,
            )
        }
    }
}