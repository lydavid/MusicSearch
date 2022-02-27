package ly.david.musicbrainzjetpackcompose.ui

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
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

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
        val currentRoute = navBackStackEntry?.destination?.route ?: Routes.SEARCH

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        val onDrawerItemClick: (String) -> Unit = { route ->
            // TODO: compile-time safety? use enum?
            navController.navigate(route) {
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
                    selectedRoute = currentRoute,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } },
                    navigateToTopLevelRoute = onDrawerItemClick
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
