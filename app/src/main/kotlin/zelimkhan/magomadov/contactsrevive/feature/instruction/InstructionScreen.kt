package zelimkhan.magomadov.contactsrevive.feature.instruction

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream10
import zelimkhan.magomadov.contactsrevive.ui.theme.Cream20
import zelimkhan.magomadov.contactsrevive.ui.theme.Divider
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface30
import zelimkhan.magomadov.contactsrevive.ui.theme.OnSurface60
import zelimkhan.magomadov.contactsrevive.ui.theme.Surface1
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning
import zelimkhan.magomadov.contactsrevive.ui.theme.Warning15

private data class InstructionStep(
    val number: Int,
    val title: String,
    val body: String,
    val warning: String? = null,
)

private val steps = listOf(
    InstructionStep(
        1,
        "Откройте Telegram на компьютере",
        "Экспорт доступен только в десктопной версии — Windows, macOS или Linux.",
        "Только десктоп"
    ),
    InstructionStep(2, "Перейдите в настройки", "Меню (☰) → Настройки → Продвинутые настройки."),
    InstructionStep(
        3,
        "Нажмите «Экспортировать данные»",
        "Прокрутите вниз до пункта «Экспортировать данные Telegram»."
    ),
    InstructionStep(
        4,
        "Выберите только контакты",
        "Снимите все галочки кроме «Список контактов». Выберите формат JSON (рекомендуется) или HTML."
    ),
    InstructionStep(
        5,
        "Нажмите «Экспортировать»",
        "Telegram создаст архив. Файл JSON — contacts.json. Файл HTML — в папке lists/contacts.html."
    ),
    InstructionStep(
        6,
        "Перенесите файл на телефон",
        "Отправьте себе в «Избранное» Telegram, через Drive или любым другим удобным способом."
    ),
    InstructionStep(
        7,
        "Выберите файл в приложении",
        "На вкладке «Импорт» нажмите «Выбрать» и укажите скачанный файл."
    ),
    InstructionStep(
        8,
        "Настройте и импортируйте",
        "Выберите контакты, включите объединение/удаление дублей и нажмите «Импортировать»."
    ),
)

@Composable
fun InstructionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        Spacer(Modifier.height(4.dp))

        WarningBanner()

        Spacer(Modifier.height(20.dp))

        FeaturesList()

        Spacer(Modifier.height(24.dp))

        Text(
            "Как это работает",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(16.dp))

        steps.forEachIndexed { index, step ->
            StepRow(step)
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(start = 19.dp)
                        .width(2.dp)
                        .height(10.dp)
                        .background(Divider),
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        FormatsRow()

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun WarningBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Warning15)
            .border(1.dp, Warning.copy(.35f), RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Text("⚠", fontSize = 17.sp)
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                "Только десктопная версия Telegram",
                style = MaterialTheme.typography.titleSmall,
                color = Warning,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Экспорт недоступен в мобильном приложении.",
                style = MaterialTheme.typography.bodySmall,
                color = Warning.copy(.75f)
            )
        }
    }
}

@Composable
private fun FeaturesList() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface1)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            "Что умеет приложение",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(2.dp))
        FeatureRow("📁", "Читает JSON и HTML файлы экспорта Telegram")
        FeatureRow("🔗", "Объединяет контакты с одинаковым именем")
        FeatureRow("🧹", "Удаляет дублирующиеся номера")
        FeatureRow("📇", "Конвертирует в VCF для импорта в любой телефон")
        FeatureRow("💾", "Хранит резервные копии контактов")
        FeatureRow("🔒", "Работает полностью офлайн — данные не покидают устройство")
    }
}

@Composable
private fun FeatureRow(icon: String, text: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(icon, fontSize = 14.sp)
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = OnSurface60)
    }
}

@Composable
private fun StepRow(step: InstructionStep) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Cream10)
                .border(1.dp, Cream.copy(.3f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                step.number.toString(),
                color = Cream,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier
            .weight(1f)
            .padding(top = 6.dp)) {
            Text(
                step.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(3.dp))
            Text(step.body, style = MaterialTheme.typography.bodySmall, color = OnSurface60)
            if (step.warning != null) {
                Spacer(Modifier.height(5.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(5.dp))
                        .background(Warning15)
                        .padding(horizontal = 7.dp, vertical = 2.dp),
                ) {
                    Text(
                        step.warning,
                        style = MaterialTheme.typography.labelSmall,
                        color = Warning
                    )
                }
            }
        }
    }
}

@Composable
private fun FormatsRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        FormatCard("JSON", "Рекомендуется\ncontacts.json", recommended = true, Modifier.weight(1f))
        FormatCard("HTML", "Папка lists/\ncontacts.html", recommended = false, Modifier.weight(1f))
    }
}

@Composable
private fun FormatCard(name: String, desc: String, recommended: Boolean, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(Surface1)
            .border(1.dp, if (recommended) Cream.copy(.35f) else Divider, RoundedCornerShape(11.dp))
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                name,
                style = MaterialTheme.typography.titleSmall,
                color = if (recommended) Cream else MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            if (recommended) {
                Spacer(Modifier.width(6.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Cream20)
                        .padding(horizontal = 5.dp, vertical = 1.dp)
                ) {
                    Text("★", style = MaterialTheme.typography.labelSmall, color = Cream)
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(desc, style = MaterialTheme.typography.labelSmall, color = OnSurface30)
    }
}