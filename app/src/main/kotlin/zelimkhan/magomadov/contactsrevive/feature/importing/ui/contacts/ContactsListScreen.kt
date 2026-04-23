package zelimkhan.magomadov.contactsrevive.feature.importing.ui.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingState
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.SelectableContact
import zelimkhan.magomadov.contactsrevive.ui.components.ContactAvatar
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream20
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1

// ── Flat list item sealed type ────────────────────────────────────────────────
private sealed interface ListItem {
    data class Header(val letter: Char) : ListItem
    data class ContactItem(val contact: SelectableContact) : ListItem
}

@Composable
internal fun ContactsListScreen(
    state: ImportingState,
    onToggle: (SelectableContact) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onImport: () -> Unit,
) {
    val selectedCount = state.contacts.count { it.isSelected }

    // Build sorted flat list grouped by first letter
    val flatList = remember(state.contacts) { state.contacts.buildFlatList() }
    val letters =
        remember(flatList) { flatList.filterIsInstance<ListItem.Header>().map { it.letter } }
    val letterToIndex = remember(flatList) {
        flatList.mapIndexedNotNull { i, item -> (item as? ListItem.Header)?.letter?.let { it to i } }
            .toMap()
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize()) {

        // ── Header bar ────────────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f)) {
                Row {
                    Text(
                        "Контактов: ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface60
                    )
                    Text(
                        state.contacts.size.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Cream,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    "Выбрано: $selectedCount",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnSurface30
                )
            }
            TextButton(onClick = onSelectAll) {
                Text(
                    "Все",
                    color = Cream,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            TextButton(onClick = onDeselectAll) {
                Text(
                    "Сбросить",
                    color = OnSurface60,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        // ── List + sidebar ────────────────────────────────────────────────────
        Box(Modifier.weight(1f)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 22.dp),
                contentPadding = PaddingValues(
                    start = 20.dp, end = 4.dp, bottom = 96.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                itemsIndexed(flatList, key = { _, item ->
                    when (item) {
                        is ListItem.Header -> "h_${item.letter}"
                        is ListItem.ContactItem -> "c_${item.contact.contact.name}_${item.contact.contact.primaryPhone}"
                    }
                }) { _, item ->
                    when (item) {
                        is ListItem.Header -> SectionHeader(item.letter)
                        is ListItem.ContactItem -> ContactRow(item.contact, onToggle)
                    }
                }
            }

            AlphabeticSidebar(
                letters = letters,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(20.dp),
                onLetterPicked = { letter ->
                    letterToIndex[letter]?.let { scope.launch { listState.scrollToItem(it) } }
                },
            )
        }

        // ── Import button ─────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 14.dp),
        ) {
            Button(
                onClick = onImport,
                enabled = selectedCount > 0 && !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cream, contentColor = Color.Black,
                    disabledContainerColor = Cream20, disabledContentColor = Cream,
                ),
            ) {
                Text(
                    text = if (state.isLoading) "Импортируем…" else "Импортировать $selectedCount",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(letter: Char) {
    Text(
        text = letter.toString(),
        style = MaterialTheme.typography.labelMedium,
        color = OnSurface30,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 14.dp, bottom = 4.dp),
    )
}

@Composable
private fun ContactRow(item: SelectableContact, onToggle: (SelectableContact) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(11.dp))
            .background(if (item.isSelected) Surface1 else Color.Transparent)
            .clickable { onToggle(item) }
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ContactAvatar(name = item.contact.name, size = 38.dp, fontSize = 14.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                text = item.contact.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (item.isSelected) MaterialTheme.colorScheme.onBackground else OnSurface60,
                fontWeight = if (item.isSelected) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
            )
            Text(
                text = if (item.contact.hasMultiplePhones)
                    "${item.contact.primaryPhone}  +${item.contact.phoneNumbers.size - 1} ещё"
                else item.contact.primaryPhone,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface30,
                maxLines = 1,
            )
        }
        Spacer(Modifier.width(8.dp))
        Icon(
            imageVector = if (item.isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (item.isSelected) Cream else OnSurface30,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun AlphabeticSidebar(
    letters: List<Char>,
    modifier: Modifier,
    onLetterPicked: (Char) -> Unit,
) {
    var height by remember { mutableStateOf(1f) }
    var active by remember { mutableStateOf<Char?>(null) }

    Column(
        modifier = modifier
            .onGloballyPositioned { height = it.size.height.toFloat().coerceAtLeast(1f) }
            .pointerInput(letters) {
                detectDragGestures(
                    onDragEnd = { active = null },
                    onDragCancel = { active = null },
                ) { change, _ ->
                    val idx = ((change.position.y / height) * letters.size).toInt()
                        .coerceIn(0, letters.lastIndex)
                    letters.getOrNull(idx)?.let { letter ->
                        if (letter != active) {
                            active = letter; onLetterPicked(letter)
                        }
                    }
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        letters.forEach { letter ->
            val isActive = letter == active
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Cream10 else Color.Transparent)
                    .clickable { onLetterPicked(letter) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = letter.toString(),
                    fontSize = 9.sp,
                    color = if (isActive) Cream else OnSurface30,
                    textAlign = TextAlign.Center,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                )
            }
        }
    }
}

// ── Helper extension ──────────────────────────────────────────────────────────

private fun List<SelectableContact>.buildFlatList(): List<ListItem> {
    val sorted = sortedBy { it.contact.name.uppercase() }
    val grouped = sorted.groupBy { it.contact.name.firstOrNull()?.uppercaseChar() ?: '#' }
    return buildList {
        grouped.entries.sortedBy { it.key }.forEach { (letter, contacts) ->
            add(ListItem.Header(letter))
            contacts.forEach { add(ListItem.ContactItem(it)) }
        }
    }
}