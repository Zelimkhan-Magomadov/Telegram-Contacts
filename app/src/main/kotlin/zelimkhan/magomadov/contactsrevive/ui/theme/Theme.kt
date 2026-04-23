package zelimkhan.magomadov.contactsrevive.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Cream,
    onPrimary = Background,
    primaryContainer = Cream20,
    onPrimaryContainer = Cream,
    background = Background,
    onBackground = OnSurface,
    surface = Surface1,
    onSurface = OnSurface,
    surfaceVariant = Surface2,
    onSurfaceVariant = OnSurface60,
    outline = Divider,
    error = Error,
    onError = Background,
    errorContainer = Error15,
)

@Composable
fun ContactsReviveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content,
    )
}