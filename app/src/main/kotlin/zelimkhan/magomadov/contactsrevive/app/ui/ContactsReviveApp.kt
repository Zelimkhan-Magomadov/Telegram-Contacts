package zelimkhan.magomadov.contactsrevive.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import zelimkhan.magomadov.contactsrevive.app.navigation.ContactsReviveNavHost
import zelimkhan.magomadov.contactsrevive.app.navigation.TopLevelDestination
import zelimkhan.magomadov.contactsrevive.ui.theme.*
import kotlin.reflect.KClass

@Composable
fun ContactsReviveApp(appState: ContactsReviveAppState) {
    val hazeState = remember { HazeState() }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            AppNavigationBar(
                topLevelDestinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination,
                hazeState = hazeState
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .hazeSource(state = hazeState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
            ) {
                val destination = appState.currentTopLevelDestination
                if (destination != null) {
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                        text = stringResource(destination.titleTextId),
                        style = MaterialTheme.typography.displayMedium,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    ContactsReviveNavHost(appState)
                }
            }
        }
    }
}

@Composable
private fun AppNavigationBar(
    topLevelDestinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    hazeState: HazeState,
) {
    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .height(72.dp)
            .clip(CircleShape)
            .hazeEffect(state = hazeState, style = HazeStyle(blurRadius = 20.dp, noiseAlpha = 0.1f))
            .background(Color.White.copy(alpha = 0.05f))
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0),
        ) {
            topLevelDestinations.forEach { destination ->
                val selected = currentDestination.isRouteInHierarchy(destination.baseRoute)
                NavigationBarItem(
                    selected = selected,
                    onClick = { navigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(if (selected) destination.selectedIcon else destination.unselectedIcon),
                            contentDescription = null,
                            tint = if (selected) Background else OnSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(destination.iconTextId),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selected) OnSurface else OnSurfaceVariant,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Primary,
                        unselectedIconColor = OnSurfaceVariant,
                        selectedIconColor = Background
                    ),
                )
            }
        }
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true
