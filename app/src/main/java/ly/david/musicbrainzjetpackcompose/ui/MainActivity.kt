package ly.david.musicbrainzjetpackcompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
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
        MainNavHost(navController = navController)
    }
}
