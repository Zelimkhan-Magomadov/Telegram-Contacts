package zelimkhan.magomadov.contactsrevive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Cream,
    onPrimary = Gray100,
    onBackground = Cream,
    onSurface = Cream
)

@Composable
fun ContactsReviveTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}