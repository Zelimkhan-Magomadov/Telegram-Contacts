package zelimkhan.magomadov.telegramcontacts.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import zelimkhan.magomadov.telegramcontacts.presentation.convert.ConvertScreen
import zelimkhan.magomadov.telegramcontacts.presentation.convert.ConvertViewModel
import zelimkhan.magomadov.telegramcontacts.presentation.ui.theme.TelegramContactsTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TelegramContactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val convertViewModel: ConvertViewModel = viewModel()
                    val convertState = convertViewModel.state.collectAsState()
                    val convertEvent = convertViewModel.event.collectAsState()
                    ConvertScreen(
                        state = convertState.value,
                        event = convertEvent.value,
                        processIntent = convertViewModel::processIntent
                    )
                }
            }
        }
    }
}