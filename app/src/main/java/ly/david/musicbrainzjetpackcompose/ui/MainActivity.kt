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
        val currentRoute = navBackStackEntry?.destination?.route ?: Routes.MAIN

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalDrawer(
            drawerContent = {
                NavigationDrawer()
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
