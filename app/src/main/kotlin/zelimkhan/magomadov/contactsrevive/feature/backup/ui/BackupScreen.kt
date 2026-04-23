package zelimkhan.magomadov.contactsrevive.feature.backup.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import zelimkhan.magomadov.contactsrevive.core.database.entity.BackupEntity
import zelimkhan.magomadov.contactsrevive.ui.components.ContactAvatar
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream20
import zelimkhan.magomadov.contactsrevive.ui.theme.Divider
import zelimkhan.magomadov.contactsrevive.ui.theme.Error
import zelimkhan.magomadov.contactsrevive.ui.theme.Error15
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Success
import zelimkhan.magomadov.contactsrevive.ui.theme.Success15
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface2
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface3
import zelimkhan.magomadov.contactsrevive.ui.util.ObserveAsEvents
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupScreen(viewModel: BackupViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(viewModel.events) { event ->
        if (event is BackupEvent.OpenVcf) openVcf(context, event.file)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Spacer(Modifier.height(4.dp))

        AutoBackupCard(
            enabled = state.isAutoBackupEnabled,
            onToggle = viewModel::setAutoBackup,
        )

        SyncButton(
            isSyncing = state.isSyncing,
            onClick = viewModel::syncNow,
        )

        AnimatedVisibility(
            visible = state.isSyncing,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                color = Cream,
                trackColor = Cream20,
            )
        }

        SyncMessageBanner(state.syncMessage, onDismiss = viewModel::dismissSyncMessage)

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "История",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(
                "${state.backups.size}",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface30
            )
        }

        if (state.backups.isEmpty()) {
            EmptyState(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    state.backups,
                    key = { it.id }) { backup ->
                    BackupCard(
                        backup = backup,
                        onTap = { viewModel.openRestoreSheet(backup) },
                        onDelete = { viewModel.delete(backup) },
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
            }
        }
    }

    // ── Restore bottom sheet ──────────────────────────────────────────────────
    val sheet = state.restoreSheet
    if (sheet != null) {
        ModalBottomSheet(
            onDismissRequest = viewModel::dismissRestoreSheet,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Surface1,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(width = 36.dp, height = 4.dp)
                        .clip(CircleShape)
                        .background(OnSurface30),
                )
            },
        ) {
            RestoreSheetContent(
                sheet = sheet,
                isSyncing = state.isSyncing,
                onToggle = viewModel::toggleRestoreContact,
                onSelectAll = viewModel::selectAllRestore,
                onDeselectAll = viewModel::deselectAllRestore,
                onRestore = viewModel::restoreSelected,
            )
        }
    }
}

// ── Sub-composables ───────────────────────────────────────────────────────────

@Composable
private fun AutoBackupCard(enabled: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface1)
            .border(1.dp, if (enabled) Cream.copy(.2f) else Divider, RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (enabled) Cream10 else Surface2),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                if (enabled) "●" else "○",
                color = if (enabled) Cream else OnSurface30,
                fontSize = 16.sp
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "Автобэкап",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text("Каждые 24 часа", style = MaterialTheme.typography.bodySmall, color = OnSurface60)
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Cream,
                uncheckedThumbColor = OnSurface30,
                uncheckedTrackColor = Surface3,
            ),
        )
    }
}

@Composable
private fun SyncButton(isSyncing: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .clickable(enabled = !isSyncing, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            Icons.Default.Sync,
            contentDescription = null,
            tint = Cream,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = if (isSyncing) "Синхронизируем…" else "Синхронизировать контакты",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSyncing) Cream else MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
private fun SyncMessageBanner(message: SyncMessage?, onDismiss: () -> Unit) {
    AnimatedVisibility(
        visible = message != null,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        if (message == null) return@AnimatedVisibility
        val (bg, color, text) = when (message) {
            SyncMessage.Success -> Triple(Success15, Success, "Синхронизация завершена ✓")
            is SyncMessage.Failure -> Triple(Error15, Error, message.message)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(bg)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onDismiss,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("✕", color = color, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun BackupCard(backup: BackupEntity, onTap: () -> Unit, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("d MMM, HH:mm", Locale("ru")).format(Date(backup.date))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .clickable(onClick = onTap)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Cream10),
            contentAlignment = Alignment.Center,
        ) { Text("💾", fontSize = 18.sp) }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                backup.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                "${backup.contactCount} контактов  ·  $dateStr",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface60,
            )
        }

        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = Error.copy(.55f),
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier) {
    Box(modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📂", fontSize = 44.sp)
            Spacer(Modifier.height(10.dp))
            Text("Нет бэкапов", style = MaterialTheme.typography.headlineSmall, color = OnSurface60)
            Text(
                "Нажмите «Синхронизировать»\nили импортируйте контакты",
                style = MaterialTheme.typography.bodyMedium, color = OnSurface30,
                textAlign = TextAlign.Center,
            )
        }
    }
}

// ── Restore sheet ─────────────────────────────────────────────────────────────

@Composable
private fun RestoreSheetContent(
    sheet: RestoreSheetState,
    isSyncing: Boolean,
    onToggle: (SelectableRestoreContact) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onRestore: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    sheet.backup.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "Выбрано: ${sheet.selectedCount} из ${sheet.contacts.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurface60
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Divider)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onSelectAll) {
                Text(
                    "Все",
                    color = Cream,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            TextButton(onClick = onDeselectAll) {
                Text(
                    "Сбросить",
                    color = OnSurface60,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            items(
                sheet.contacts,
                key = { it.contact.name + it.contact.primaryPhone }) { item ->
                RestoreContactRow(item = item, onToggle = { onToggle(item) })
            }
        }
        Spacer(Modifier.height(14.dp))
        Button(
            onClick = onRestore,
            enabled = sheet.selectedCount > 0 && !isSyncing,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Cream, contentColor = Color.Black,
                disabledContainerColor = Cream20, disabledContentColor = Cream,
            ),
        ) {
            Text(
                if (isSyncing) "Восстанавливаем…" else "Восстановить ${sheet.selectedCount}",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun RestoreContactRow(item: SelectableRestoreContact, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (item.isSelected) Surface2 else Color.Transparent)
            .clickable(onClick = onToggle)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ContactAvatar(name = item.contact.name, size = 34.dp, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(
                item.contact.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isSelected) MaterialTheme.colorScheme.onBackground else OnSurface60,
                fontWeight = if (item.isSelected) FontWeight.Medium else FontWeight.Normal,
                maxLines = 1, overflow = TextOverflow.Ellipsis,
            )
            Text(
                item.contact.primaryPhone,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface30
            )
        }
        Icon(
            imageVector = if (item.isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (item.isSelected) Cream else OnSurface30,
            modifier = Modifier.size(18.dp),
        )
    }
}

// ── Open VCF ──────────────────────────────────────────────────────────────────

private fun openVcf(context: Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    context.startActivity(
        Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "text/x-vcard")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    )
}