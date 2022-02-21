package ly.david.musicbrainzjetpackcompose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.musicbrainzjetpackcompose.R
import ly.david.musicbrainzjetpackcompose.common.fromJson
import ly.david.musicbrainzjetpackcompose.common.toJson
import ly.david.musicbrainzjetpackcompose.data.Artist
import ly.david.musicbrainzjetpackcompose.ui.artist.ArtistScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.discovery.SearchScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.release.ReleaseScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.releasegroup.ReleaseGroupScreenScaffold

object Routes {
    // Not a route. Just used to split sub-routes.
    private const val DIVIDER = "/"

    private const val ARTIST = "artist"
    private const val RELEASE_GROUP = "release-group"
    private const val RELEASE = "release"

    /**
     * Search MusicBrainz for resources with a string query.
     * Right now, we only support searching artists.
     */
    const val DISCOVER = "discover"

    /**
     * Information about an artist.
     * Currently, we only show a list of their release groups.
     */
    const val DISCOVER_ARTIST = "$DISCOVER$DIVIDER$ARTIST"

    /**
     * Information about a release group.
     * Currently, we only show a list of its releases.
     */
    const val DISCOVER_RELEASE_GROUP = "$DISCOVER$DIVIDER$RELEASE_GROUP"

    /**
     * Information about a release.
     * Currently, we only show a list of its tracks.
     */
    const val DISCOVER_RELEASE = "$DISCOVER$DIVIDER$RELEASE"

    /**
     * Shows a the user's most recently visited screens.
     */
    const val HISTORY = "history"

    fun getTopLevelRoute(route: String): String = route.split(DIVIDER).first()
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)
    val releaseGroupDeeplink = stringResource(id = R.string.route_release_group)
    val releaseDeeplink = stringResource(id = R.string.route_release)

    NavHost(
        navController = navController,
        startDestination = Routes.DISCOVER,
    ) {

        val onBack = {
            // Consume return value so that we don't have to specify Boolean return type when passing this function
            val consumed = navController.popBackStack()
        }

        val onHomeClick: () -> Unit = {
            navController.navigate(Routes.DISCOVER) {
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
            navController.navigate("${Routes.DISCOVER_ARTIST}/$artistJson")
            // TODO: seems like even without restoreState = true here, we keep the search results
            //  also, it looks like we don't need another api call! Well, that's because we don't call viewmodel until
            //  user hits search
        }

        composable(Routes.DISCOVER) {
            SearchScreenScaffold(
                onArtistClick = onArtistClick,
                openDrawer = openDrawer
            )
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Routes.DISCOVER_RELEASE_GROUP}/$releaseGroupId") {
                // TODO: This let us return to this screen in the same position, but doesn't prevent another api all
                //  since we're always calling at start
                restoreState = true
            }
        }

        // TODO: use id, and update title from response
        composable(
            route = "${Routes.DISCOVER_ARTIST}/{artistJson}",
            arguments = listOf(
                navArgument("artistJson") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://artist/{artistJson}"
                }
            )
        ) { entry ->
            val artistJson = entry.arguments?.getString("artistJson") ?: return@composable
            val artist = artistJson.fromJson(Artist::class.java)
            if (artist != null) {
                ArtistScreenScaffold(
                    artist = artist,
                    onReleaseGroupClick = onReleaseGroupClick,
                    onBack = onBack
                )
            }
        }

        val onReleaseClick: (String) -> Unit = { releaseId ->
            navController.navigate("${Routes.DISCOVER_RELEASE}/$releaseId") {
                restoreState = true
            }
        }

        composable(
            route = "${Routes.DISCOVER_RELEASE_GROUP}/{releaseGroupId}",
            arguments = listOf(
                navArgument("releaseGroupId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$releaseGroupDeeplink/{releaseGroupId}"
                }
            )
        ) { entry ->
            val releaseGroupId = entry.arguments?.getString("releaseGroupId") ?: return@composable
            ReleaseGroupScreenScaffold(
                releaseGroupId = releaseGroupId,
                onReleaseClick = onReleaseClick,
                onBack = onBack
            )
        }

        composable(
            "${Routes.DISCOVER_RELEASE}/{releaseId}",
            arguments = listOf(
                navArgument("releaseId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release/165f6643-2edb-4795-9abe-26bd0533e59d" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$releaseDeeplink/{releaseId}"
                }
            )
        ) { entry ->
            val releaseId = entry.arguments?.getString("releaseId") ?: return@composable
            ReleaseScreenScaffold(
                releaseId = releaseId,
                onBack = onBack
            )
        }
    }
}
