package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

@Composable
internal fun MainApp() {
    MusicBrainzJetpackComposeTheme {

        val navController = rememberNavController()

        val coroutineScope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // Note that destination?.route includes parameters such as artistId
        val currentRoute = navBackStackEntry?.destination?.route ?: Destination.LOOKUP.route
        val currentTopLevelDestination: Destination = currentRoute.getTopLevelRoute().getTopLevelDestination()

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val onTopLevelDestinationClick: (Destination) -> Unit = { topLevelDestination ->
            navController.navigate(topLevelDestination.name) {
                // Top-level screens should use this to prevent selecting the same screen.
                launchSingleTop = true

                // Selecting a top-level screen should remove all backstack.
                popUpTo(navController.graph.findStartDestination().id) {
                    // And it should not save the state of the previous screen.
                    saveState = false
                }
            }
        }

        ModalDrawer(
            drawerContent = {
                NavigationDrawer(
                    selectedTopLevelDestination = currentTopLevelDestination,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } },
                    navigateToTopLevelDestination = onTopLevelDestinationClick
                )
            },
            drawerState = drawerState
        ) {
            NavigationGraph(
                navController = navController,
                openDrawer = { coroutineScope.launch { drawerState.open() } }
            )
        }
    }
}
