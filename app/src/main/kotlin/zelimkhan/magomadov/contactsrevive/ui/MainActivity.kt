package zelimkhan.magomadov.contactsrevive.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import zelimkhan.magomadov.contactsrevive.ui.conversion.ConversionScreen
import zelimkhan.magomadov.contactsrevive.ui.theme.ContactsReviveTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsReviveTheme {
                ConversionScreen()
            }
        }
    }
}