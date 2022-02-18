package ly.david.musicbrainzjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.musicbrainzjetpackcompose.common.fromJson
import ly.david.musicbrainzjetpackcompose.common.toJson
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.ui.artist.ArtistScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.discovery.SearchScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.releasegroup.ReleaseGroupScreenScaffold
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
internal fun MainApp() {
    MusicBrainzJetpackComposeTheme {
        val navController = rememberNavController()
        MainNavHost(navController = navController)
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

        val onArtistClick: (Artist) -> Unit = { artist ->
            val artistJson = artist.toJson()
            navController.navigate("artist/$artistJson")
        }

        composable("main") {
            SearchScreenScaffold(onArtistClick = onArtistClick)
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
//            val releaseGroupJson = releaseGroup.toJson()
            navController.navigate("release-group/$releaseGroupId")
        }

        composable(
            "artist/{artistJson}",
            arguments = listOf(
                navArgument("artistJson") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mbjc://artist/{artistJson}"
                }
            )
        ) { entry ->
            val artistJson = entry.arguments?.getString("artistJson") ?: return@composable
            val artist = artistJson.fromJson(Artist::class.java)
            if (artist != null) {
                ArtistScreenScaffold(artist, onReleaseGroupClick)
            }
        }

        // TODO: let's prefer to pass an id, and let the screen query the data itself
        composable(
            "release-group/{releaseGroupId}",
            arguments = listOf(
                navArgument("releaseGroupId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Test deeplink: adb shell am start -d "mbjc://release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mbjc://release-group/{releaseGroupId}"
                }
            )
        ) { entry ->
            val releaseGroupId = entry.arguments?.getString("releaseGroupId") ?: return@composable
//            val releaseGroup = releaseGroupJson.fromJson(ReleaseGroup::class.java)
//            if (releaseGroup != null) {
            ReleaseGroupScreenScaffold(releaseGroupId)
//            }
        }
    }
}
