package zelimkhan.magomadov.contactsrevive.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import zelimkhan.magomadov.contactsrevive.app.ui.ContactsReviveAppState
import zelimkhan.magomadov.contactsrevive.feature.backup.navigation.backupScreen
import zelimkhan.magomadov.contactsrevive.feature.importing.navigation.ImportingRoute
import zelimkhan.magomadov.contactsrevive.feature.importing.navigation.importingScreen
import zelimkhan.magomadov.contactsrevive.feature.instruction.navigation.instructionScreen

@Composable
fun ContactsReviveNavHost(
    appState: ContactsReviveAppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = ImportingRoute,
        modifier = modifier
    ) {
        importingScreen()
        backupScreen()
        instructionScreen()
    }
}