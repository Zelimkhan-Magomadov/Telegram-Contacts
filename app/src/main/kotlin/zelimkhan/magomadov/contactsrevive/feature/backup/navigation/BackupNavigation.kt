package zelimkhan.magomadov.contactsrevive.feature.backup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import zelimkhan.magomadov.contactsrevive.feature.backup.BackupScreen

@Serializable
data object BackupRoute

fun NavController.navigateToBackup(navOptions: NavOptions) {
    return navigate(route = BackupRoute, navOptions = navOptions)
}

fun NavGraphBuilder.backupScreen() {
    composable<BackupRoute> {
        BackupScreen()
    }
}