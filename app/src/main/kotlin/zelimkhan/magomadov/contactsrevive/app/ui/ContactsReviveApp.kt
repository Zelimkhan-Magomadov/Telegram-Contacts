package zelimkhan.magomadov.contactsrevive.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import zelimkhan.magomadov.contactsrevive.app.navigation.ContactsReviveNavHost
import zelimkhan.magomadov.contactsrevive.app.navigation.TopLevelDestination
import zelimkhan.magomadov.contactsrevive.ui.theme.BlackGray
import kotlin.reflect.KClass

@Composable
fun ContactsReviveApp(
    appState: ContactsReviveAppState,
    modifier: Modifier = Modifier,
) {
    ContactsReviveApp(
        appState = appState
    )
}

@Composable
private fun ContactsReviveApp(
    appState: ContactsReviveAppState,
) {
    Scaffold(
        containerColor = Color.Black,
        contentColor = MaterialTheme.colorScheme.onBackground,
        bottomBar = {
            ContactReviveNavigationBar(
                topLevelDestinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)),
        ) {
            val destination = appState.currentTopLevelDestination
            var shouldShowTopAppBar = false

            if (destination != null) {
                shouldShowTopAppBar = true
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    text = stringResource(destination.titleTextId),
                    style = MaterialTheme.typography.displayLarge,
                )
            }

            Box(
                modifier = Modifier.consumeWindowInsets(
                    when (shouldShowTopAppBar) {
                        true -> WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                        false -> WindowInsets(0, 0, 0, 0)
                    }
                )
            ) {
                ContactsReviveNavHost(appState)
            }
        }
    }
}

@Composable
fun ContactReviveNavigationBar(
    topLevelDestinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
            .height(64.dp)
            .clip(CircleShape)
            .background(color = BlackGray)
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0),
        ) {
            topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.baseRoute)
                NavigationBarItem(
                    selected = false,
                    onClick = { navigateToTopLevelDestination(destination) },
                    icon = {
                        val currentIcon = when (selected) {
                            true -> destination.selectedIcon
                            false -> destination.unselectedIcon
                        }

                        Image(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(currentIcon),
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.iconTextId),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean {
    return this?.hierarchy?.any { it.hasRoute(route) } == true
}

@Preview
@Composable
private fun Preview() {
    ContactsReviveApp(appState = rememberContactsReviveAppState())
}