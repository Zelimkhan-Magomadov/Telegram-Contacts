package zelimkhan.magomadov.contactsrevive.feature.importing.ui.processing

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingState
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Divider
import zelimkhan.magomadov.contactsrevive.ui.theme.Error
import zelimkhan.magomadov.contactsrevive.ui.theme.Error15
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface2
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface3
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning15

@Composable
internal fun ProcessingOptionsScreen(
    state: ImportingState,
    onMergeToggle: (Boolean) -> Unit,
    onDedupToggle: (Boolean) -> Unit,
    onShowPreview: () -> Unit,
    onContinue: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(Modifier.height(4.dp))

        StatsRow(state)

        Spacer(Modifier.height(24.dp))

        Text("Обработка", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            "Настройте контакты перед импортом",
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurface60,
        )

        Spacer(Modifier.height(16.dp))

        OptionRow(
            title = "Объединить по имени",
            subtitle = "Контакты с одинаковым именем → одна запись",
            badge = state.mergeGroups.size.takeIf { it > 0 }?.let { "$it групп" },
            badgeColor = Warning,
            badgeBg = Warning15,
            checked = state.mergeByName,
            onChecked = onMergeToggle,
        )

        Spacer(Modifier.height(10.dp))

        OptionRow(
            title = "Убрать дубликаты",
            subtitle = "Одинаковые номера телефонов будут удалены",
            badge = state.duplicateGroups.size.takeIf { it > 0 }?.let { "$it групп" },
            badgeColor = Error,
            badgeBg = Error15,
            checked = state.removeDuplicates,
            onChecked = onDedupToggle,
        )

        val hasChanges = (state.mergeByName && state.mergeGroups.isNotEmpty()) ||
                (state.removeDuplicates && state.duplicateGroups.isNotEmpty())

        if (hasChanges) {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = onShowPreview,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Divider),
            ) {
                Text(
                    "Посмотреть изменения →",
                    color = OnSurface60,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream,
                contentColor = Color.Black
            ),
        ) {
            Text("Выбрать контакты", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun StatsRow(state: ImportingState) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatChip("Всего", state.totalRaw.toString(), Cream, Modifier.weight(1f))
        StatChip("Объединить", state.mergeGroups.size.toString(), Warning, Modifier.weight(1f))
        StatChip("Дубли", state.duplicateGroups.size.toString(), Error, Modifier.weight(1f))
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface2)
            .padding(vertical = 12.dp, horizontal = 8.dp),
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

@Composable
private fun OptionRow(
    title: String,
    subtitle: String,
    badge: String?,
    badgeColor: Color,
    badgeBg: Color,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface1)
            .border(
                1.dp,
                if (checked) badgeColor.copy(.25f) else Divider,
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                if (badge != null) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(badgeBg)
                            .padding(horizontal = 7.dp, vertical = 2.dp),
                    ) {
                        Text(badge, style = MaterialTheme.typography.labelSmall, color = badgeColor)
                    }
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = OnSurface60)
        }
        Spacer(Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onChecked,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Cream,
                uncheckedThumbColor = OnSurface30,
                uncheckedTrackColor = Surface3,
            ),
        )
    }
}