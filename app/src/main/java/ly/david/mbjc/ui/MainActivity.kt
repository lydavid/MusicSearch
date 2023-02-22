package ly.david.mbjc.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ly.david.data.navigation.Destination
import ly.david.data.navigation.getTopLevelDestination
import ly.david.data.navigation.getTopLevelRoute
import ly.david.mbjc.ui.navigation.BottomNavigationBar
import ly.david.mbjc.ui.navigation.NavigationGraph
import ly.david.mbjc.ui.settings.AppPreferences
import ly.david.mbjc.ui.settings.useDarkTheme
import ly.david.mbjc.ui.settings.useMaterialYou
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
                darkTheme = appPreferences.useDarkTheme(),
                materialYou = appPreferences.useMaterialYou()
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // Note that destination?.route includes parameters such as artistId
    val currentRoute = navBackStackEntry?.destination?.route ?: Destination.LOOKUP.route
    val currentTopLevelDestination: Destination = currentRoute.getTopLevelRoute().getTopLevelDestination()

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

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTopLevelDestination = currentTopLevelDestination,
                navigateToTopLevelDestination = { it.onTopLevelDestinationClick() }
            )
        }
    ) { innerPadding ->

        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
