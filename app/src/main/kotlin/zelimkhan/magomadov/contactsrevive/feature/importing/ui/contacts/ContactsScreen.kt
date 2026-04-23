package zelimkhan.magomadov.contactsrevive.feature.importing.ui.contacts

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.ImportingState
import zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing.SelectableContact
import zelimkhan.magomadov.contactsrevive.ui.components.AlphabeticalScroller
import zelimkhan.magomadov.contactsrevive.ui.theme.Background
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurfaceVariant
import zelimkhan.magomadov.contactsrevive.ui.theme.Primary
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface
import zelimkhan.magomadov.contactsrevive.ui.theme.SurfaceVariant

@Composable
fun ContactsScreen(
    state: ImportingState,
    onContactToggle: (SelectableContact) -> Unit,
    onSave: () -> Unit,
) {
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Группируем контакты по первой букве для алфавитной навигации и заголовков
    val groupedContacts = remember(state.contacts) {
        state.contacts.groupBy { it.contact.name.firstOrNull()?.uppercase() ?: "#" }
    }
    val alphabet = remember(groupedContacts) { groupedContacts.keys.toList().sorted() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Заголовок статистики (как в Figma: Контактов: 45)
            Row(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = OnSurface,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Контактов: ${state.contacts.size}",
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 48.dp, // Место для скроллера
                        bottom = 100.dp, // Место для кнопки
                        top = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    groupedContacts.forEach { (letter, contacts) ->
                        itemsIndexed(contacts) { index, selectableContact ->
                            ContactRow(
                                letter = if (index == 0) letter else null,
                                selectableContact = selectableContact,
                                onToggle = { onContactToggle(selectableContact) }
                            )
                        }
                    }
                }

                // Алфавитный скроллер справа
                AlphabeticalScroller(
                    alphabet = alphabet,
                    onLetterSelected = { letter ->
                        val index = state.contacts.indexOfFirst {
                            it.contact.name.startsWith(letter, ignoreCase = true)
                        }
                        if (index != -1) {
                            scope.launch { scrollState.scrollToItem(index) }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                )
            }
        }

        // Кнопка импорта снизу
        if (state.contacts.any { it.isSelected }) {
            FloatingActionButton(
                onClick = onSave,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 110.dp) // Учитываем Haze BottomBar
                    .fillMaxWidth(0.8f)
                    .height(56.dp),
                containerColor = Primary,
                contentColor = Background,
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Импортировать (${state.contacts.count { it.isSelected }})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ContactRow(
    letter: String?,
    selectableContact: SelectableContact,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Буква слева от карточки (только для первого элемента в группе)
        Box(
            modifier = Modifier.width(40.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (letter != null) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.titleLarge,
                    color = OnSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Карточка контакта
        val contact = selectableContact.contact
        Surface(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
                .clickable { onToggle() },
            color = if (selectableContact.isSelected) SurfaceVariant else Surface,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Аватарка с буквой
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.name.take(1).uppercase(),
                        color = Background,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                        maxLines = 1
                    )
                    Text(
                        text = contact.phoneNumbers.firstOrNull() ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant
                    )
                }
            }
        }
    }
}
