package ly.david.musicbrainzjetpackcompose.ui

import android.util.Log
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
import ly.david.musicbrainzjetpackcompose.ui.artist.ArtistScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.release.ReleaseScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.releasegroup.ReleaseGroupScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.search.SearchScreenScaffold

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
        startDestination = Routes.SEARCH,
    ) {

        val onBack = {
            // Consume return value so that we don't have to specify Boolean return type when passing this function
            val consumed = navController.popBackStack()
        }

        val onHomeClick: () -> Unit = {
            navController.navigate(Routes.SEARCH) {
                // Top-level screens should use this to prevent selecting the same screen
                launchSingleTop = true

                // Selecting a top-level screen should remove all backstack
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }
        }

        val onArtistClick: (String) -> Unit = { artistId ->
//            val artistJson = artist.toJson()
            navController.navigate("${Routes.LOOKUP_ARTIST}/$artistId")
            // TODO: seems like even without restoreState = true here, we keep the search results
            //  also, it looks like we don't need another api call! Well, that's because we don't call viewmodel until
            //  user hits search
        }

        composable(Routes.SEARCH) {
            SearchScreenScaffold(
                openDrawer = openDrawer,
                onArtistClick = onArtistClick
            )
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Routes.LOOKUP_RELEASE_GROUP}/$releaseGroupId") {
                // TODO: This let us return to this screen in the same position, but doesn't prevent another api all
                //  since we're always calling at start
                restoreState = true
            }
        }

        // TODO: use id, and update title from response
        composable(
            route = "${Routes.LOOKUP_ARTIST}/{artistId}",
            arguments = listOf(
                navArgument("artistId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://artist/{artistId}"
                }
            )
        ) { entry ->
            val artistId = entry.arguments?.getString("artistId") ?: return@composable
            ArtistScreenScaffold(
                artistId = artistId,
                onReleaseGroupClick = onReleaseGroupClick,
                onBack = onBack
            )
        }

        // TODO: we can generalize all "lookup" routes that only needs an id
        //  together with a string/enum for route, we can navigate to appropriate screen
        //  Then we can pass that to history/drawer
        val onReleaseClick: (String) -> Unit = { releaseId ->
            navController.navigate("${Routes.LOOKUP_RELEASE}/$releaseId") {
                restoreState = true
            }
        }

        composable(
            route = "${Routes.LOOKUP_RELEASE_GROUP}/{releaseGroupId}",
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
            "${Routes.LOOKUP_RELEASE}/{releaseId}",
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
            ) {
                Log.d("Remove This", "NavigationGraph: Clicked recording with id=${it}")
            }
        }
    }
}
