package zelimkhan.magomadov.contactsrevive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,
    onPrimary = DarkBackground,
    secondary = Cream,
    onSecondary = DarkBackground,
    background = DarkBackground,
    onBackground = Cream,
    surface = SurfaceColor,
    onSurface = Cream,
    error = ErrorColor
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