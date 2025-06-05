package zelimkhan.magomadov.contactsrevive.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import zelimkhan.magomadov.contactsrevive.app.ui.ContactsReviveApp
import zelimkhan.magomadov.contactsrevive.app.ui.rememberContactsReviveAppState
import zelimkhan.magomadov.contactsrevive.ui.theme.ContactsReviveTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
            ) { false },
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = lightScrim,
                darkScrim = darkScrim,
            ) { false },
        )
        setContent {
            val appState = rememberContactsReviveAppState()
            ContactsReviveTheme {
                ContactsReviveApp(appState)
            }
        }
    }
}

private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)