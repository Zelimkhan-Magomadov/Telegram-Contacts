package zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Divider
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Success
import zelimkhan.magomadov.contactsrevive.ui.theme.Success15
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning

@Composable
internal fun ImportResultScreen(
    state: ImportingState,
    onAddToPhone: () -> Unit,
    onShare: () -> Unit,
    onStartOver: () -> Unit,
) {
    val summary = state.summary ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Success15),
            contentAlignment = Alignment.Center,
        ) {
            Text("✓", fontSize = 40.sp, color = Success)
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Готово!",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Контакты сконвертированы в VCF",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurface60,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(28.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SummaryChip("Импортировано", summary.imported.toString(), Cream, Modifier.weight(1f))
            if (summary.mergedGroups > 0)
                SummaryChip(
                    "Объединено",
                    summary.mergedGroups.toString(),
                    Warning,
                    Modifier.weight(1f)
                )
            if (summary.removedDuplicates > 0)
                SummaryChip(
                    "Удалено",
                    summary.removedDuplicates.toString(),
                    OnSurface60,
                    Modifier.weight(1f)
                )
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onAddToPhone,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream,
                contentColor = Color.Black
            ),
        ) {
            Text("Добавить в телефон", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Divider),
        ) {
            Text("Поделиться VCF", color = OnSurface60, style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(14.dp))

        TextButton(onClick = onStartOver) {
            Text(
                "Импортировать ещё один файл",
                color = OnSurface30,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun SummaryChip(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .border(1.dp, color.copy(.2f), RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            value,
            style = MaterialTheme.typography.displaySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(label, style = MaterialTheme.typography.labelSmall, color = OnSurface30)
    }
}