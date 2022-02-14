package ly.david.musicbrainzjetpackcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ControlCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.musicbrainzjetpackcompose.ui.discovery.ArtistScreen
import ly.david.musicbrainzjetpackcompose.ui.discovery.SearchScreen
import ly.david.musicbrainzjetpackcompose.ui.theme.MusicBrainzJetpackComposeTheme

class MainActivity : ComponentActivity() {

//    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainApp()
        }
    }
}

// area, artist, event, genre, instrument, label, place, recording, release, release-group, series, work, url
enum class QueryResources(val displayText: String, val queryText: String) {
    AREA("Area", "area"),
    ARTIST("Artist", "artist"),
    EVENT("Event", "event"),
    GENRE("Genre", "genre"),
    INSTRUMENT("Instrument", "instrument"),
    LABEL("Label", "label"),
    PLACE("Place", "place"),
    RECORDING("Recording", "recording"),
    RELEASE("Release", "release"),
    RELEASE_GROUP("Release Group", "release_group"),
    SERIES("Series", "series"),
    WORK("Work", "work"),
    URL("URL", "url")
}

@Composable
internal fun MainApp(
    // This only works if our ViewModel has no parameters. Otherwise we will need Hilt. Or by viewModels() from Activity.
) {
    MusicBrainzJetpackComposeTheme {

        val navController = rememberNavController()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Search Artists") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.ControlCamera, contentDescription = "Query for artists")
                        }
                    },
                    backgroundColor = Color.White
                )
            }
        ) {
            MainNavHost(navController = navController)
        }
    }
}

@Composable
internal fun MainNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "main",
    ) {

        val onArtistClick: (String) -> Unit = { id ->
            navController.navigate("artist/$id")
        }

        composable("main") {
            SearchScreen(onClickArtist = onArtistClick)
        }

        composable(
            "artist/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Test deeplink: adb shell am start -d "mbjc://artist/6825ace2-3563-4ac5-8d85-c7bf1334bd2c" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mbjc://artist/{id}"
                }
            )
        ) { entry ->
            val artistId = entry.arguments?.getString("id")
            // TODO: debug is printed twice, despite only adding this screen to backstack once
            Log.d("Remove This", "MainNavHost: congrats, we passed $artistId")
//            SingleAccountBody(account = account)
            if (artistId != null) {
                ArtistScreen(artistId)
            }
        }
    }
}
