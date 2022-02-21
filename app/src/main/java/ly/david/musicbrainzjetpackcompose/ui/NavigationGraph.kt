package ly.david.musicbrainzjetpackcompose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.musicbrainzjetpackcompose.common.fromJson
import ly.david.musicbrainzjetpackcompose.common.toJson
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.ui.artist.ArtistScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.discovery.SearchScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.release.ReleaseScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.releasegroup.ReleaseGroupScreenScaffold

object Routes {
    const val MAIN = "main"
    const val ARTIST = "artist"
    const val RELEASE_GROUP = "release-group"
    const val RELEASE = "release"
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
    ) {

        val onHomeClick: () -> Unit = {
            navController.navigate(Routes.MAIN) {
                // Top-level screens should use this to prevent selecting the same screen
                launchSingleTop = true

                // Selecting a top-level screen should remove all backstack
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }

        val onArtistClick: (Artist) -> Unit = { artist ->
            val artistJson = artist.toJson()
            navController.navigate("${Routes.ARTIST}/$artistJson")
            // TODO: seems like even without restoreState = true here, we keep the search results
            //  also, it looks like we don't need another api call! Well, that's because we don't call viewmodel until
            //  user hits search
        }

        composable(Routes.MAIN) {
            SearchScreenScaffold(onArtistClick = onArtistClick, openDrawer = openDrawer)
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Routes.RELEASE_GROUP}/$releaseGroupId") {
                // TODO: This let us return to this screen in the same position, but doesn't prevent another api all
                //  since we're always calling at start
                restoreState = true
            }
        }

        // TODO: use id, and update title from response
        composable(
            route = "${Routes.ARTIST}/{artistJson}",
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

        val onReleaseClick: (String) -> Unit = { releaseId ->
            navController.navigate("${Routes.RELEASE}/$releaseId") {
                restoreState = true
            }
        }

        composable(
            route = "${Routes.RELEASE_GROUP}/{releaseGroupId}",
            arguments = listOf(
                navArgument("releaseGroupId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mbjc://release-group/{releaseGroupId}"
                }
            )
        ) { entry ->
            val releaseGroupId = entry.arguments?.getString("releaseGroupId") ?: return@composable
            ReleaseGroupScreenScaffold(releaseGroupId, onReleaseClick)
        }

        composable(
            "${Routes.RELEASE}/{releaseId}",
            arguments = listOf(
                navArgument("releaseId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release/165f6643-2edb-4795-9abe-26bd0533e59d" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "mbjc://release/{releaseId}"
                }
            )
        ) { entry ->
            val releaseId = entry.arguments?.getString("releaseId") ?: return@composable
            ReleaseScreenScaffold(releaseId)
        }
    }
}
