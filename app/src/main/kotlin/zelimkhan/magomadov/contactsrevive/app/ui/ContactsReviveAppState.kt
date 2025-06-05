package zelimkhan.magomadov.contactsrevive.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import zelimkhan.magomadov.contactsrevive.app.navigation.TopLevelDestination
import zelimkhan.magomadov.contactsrevive.feature.backup.navigation.navigateToBackup
import zelimkhan.magomadov.contactsrevive.feature.importing.navigation.navigateToImporting
import zelimkhan.magomadov.contactsrevive.feature.instruction.navigation.navigateToInstruction

@Composable
fun rememberContactsReviveAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): ContactsReviveAppState {
    return remember(
        navController,
        coroutineScope,
    ) {
        ContactsReviveAppState(
            navController = navController,
            coroutineScope = coroutineScope,
        )
    }
}

@Stable
class ContactsReviveAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry =
                navController.currentBackStackEntryFlow.collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.IMPORTING -> navController.navigateToImporting(topLevelNavOptions)
            TopLevelDestination.BACKUP -> navController.navigateToBackup(topLevelNavOptions)
            TopLevelDestination.INSTRUCTION -> navController.navigateToInstruction(
                topLevelNavOptions
            )
        }
    }
}