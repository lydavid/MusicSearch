package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch
import ly.david.data.navigation.Destination
import ly.david.data.navigation.getTopLevelDestination
import ly.david.data.navigation.getTopLevelRoute
import ly.david.mbjc.ui.navigation.NavigationDrawer
import ly.david.mbjc.ui.navigation.NavigationGraph
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.settings.shouldUseDarkColors
import ly.david.mbjc.ui.theme.BaseTheme

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            BaseTheme(
                context = this,
                darkTheme = appPreferences.shouldUseDarkColors()
            ) {
                MainApp(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainApp(
    navController: NavHostController
) {

    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Note that destination?.route includes parameters such as artistId
    val currentRoute = navBackStackEntry?.destination?.route ?: Destination.LOOKUP.route
    val currentTopLevelDestination: Destination = currentRoute.getTopLevelRoute().getTopLevelDestination()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val onTopLevelDestinationClick: Destination.() -> Unit = {
        navController.navigate(name) {
            // Top-level screens should use this to prevent selecting the same screen.
            launchSingleTop = true

            // Selecting a top-level screen should remove all backstack.
            popUpTo(navController.graph.findStartDestination().id) {
                // And it should not save the state of the previous screen.
                saveState = false
            }
        }
    }

    ModalNavigationDrawer(
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
