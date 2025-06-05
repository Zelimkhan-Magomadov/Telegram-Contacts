package zelimkhan.magomadov.contactsrevive.feature.importing.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import zelimkhan.magomadov.contactsrevive.feature.importing.ImportingScreen

@Serializable
data object ImportingRoute

fun NavController.navigateToImporting(navOptions: NavOptions) {
    return navigate(route = ImportingRoute, navOptions = navOptions)
}

fun NavGraphBuilder.importingScreen() {
    composable<ImportingRoute> {
        ImportingScreen()
    }
}