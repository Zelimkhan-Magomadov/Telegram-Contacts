package zelimkhan.magomadov.contactsrevive.feature.importing.ui.changes

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.DuplicateGroup
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingState
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.MergeGroup
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10
import zelimkhan.magomadov.contactsrevive.ui.theme.Divider
import zelimkhan.magomadov.contactsrevive.ui.theme.Error
import zelimkhan.magomadov.contactsrevive.ui.theme.Error15
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning

@Composable
internal fun ChangesPreviewScreen(
    state: ImportingState,
    onApply: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (state.mergeByName && state.mergeGroups.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(4.dp))
                    SectionLabel("Будут объединены", "${state.mergeGroups.size} групп", Warning)
                    Spacer(Modifier.height(8.dp))
                }
                items(state.mergeGroups, key = { it.name }) { group ->
                    MergeGroupCard(group)
                }
                item { Spacer(Modifier.height(12.dp)) }
            }

            if (state.removeDuplicates && state.duplicateGroups.isNotEmpty()) {
                item {
                    SectionLabel("Дубликаты удалятся", "${state.duplicateGroups.size} групп", Error)
                    Spacer(Modifier.height(8.dp))
                }
                items(state.duplicateGroups, key = { it.phone }) { group ->
                    DuplicateGroupCard(group)
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 14.dp),
        ) {
            Button(
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cream,
                    contentColor = Color.Black
                ),
            ) {
                Text("Применить и выбрать контакты", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun SectionLabel(title: String, sub: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(6.dp))
        Text(sub, style = MaterialTheme.typography.labelSmall, color = OnSurface30)
    }
}

@Composable
private fun MergeGroupCard(group: MergeGroup) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .padding(14.dp),
    ) {
        Text(
            group.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        group.phones.forEachIndexed { idx, phone ->
            if (idx > 0) HorizontalDivider(
                Modifier.padding(vertical = 4.dp),
                color = Divider,
                thickness = .5.dp
            )
            PhoneRow(
                phone,
                if (idx == 0) Cream else OnSurface60,
                if (idx == 0) "основной" else null,
                Cream10,
                Cream
            )
        }
    }
}

@Composable
private fun DuplicateGroupCard(group: DuplicateGroup) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .padding(14.dp),
    ) {
        Text(
            group.phone,
            style = MaterialTheme.typography.titleSmall,
            color = Error,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        group.names.forEachIndexed { idx, name ->
            if (idx > 0) HorizontalDivider(
                Modifier.padding(vertical = 4.dp),
                color = Divider,
                thickness = .5.dp
            )
            PhoneRow(
                name,
                if (idx == 0) MaterialTheme.colorScheme.onBackground else OnSurface60,
                if (idx > 0) "удалить" else null,
                Error15,
                Error
            )
        }
    }
}

@Composable
private fun PhoneRow(text: String, textColor: Color, tag: String?, tagBg: Color, tagColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        if (tag != null) {
            Box(
                Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(tagBg)
                    .padding(horizontal = 6.dp, vertical = 1.dp)
            ) {
                Text(tag, style = MaterialTheme.typography.labelSmall, color = tagColor)
            }
        }
    }
}