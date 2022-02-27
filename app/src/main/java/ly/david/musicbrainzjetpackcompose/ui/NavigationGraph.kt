package ly.david.musicbrainzjetpackcompose.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.musicbrainzjetpackcompose.R
import ly.david.musicbrainzjetpackcompose.ui.artist.ArtistScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.history.HistoryScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.release.ReleaseScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.releasegroup.ReleaseGroupScreenScaffold
import ly.david.musicbrainzjetpackcompose.ui.search.SearchScreenScaffold

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)
    val artistDeeplink = stringResource(id = R.string.deeplink_artist)
    val releaseGroupDeeplink = stringResource(id = R.string.deeplink_release_group)
    val releaseDeeplink = stringResource(id = R.string.deeplink_release)

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val onBack = {
            // Consume return value so that we don't have to specify Boolean return type when passing this function
            val consumed = navController.popBackStack()
        }

        val onArtistClick: (String) -> Unit = { artistId ->
            // TODO: these should be built by Destination
            navController.navigate("${Destination.LOOKUP_ARTIST.route}/$artistId") {
                restoreState = true
            }
        }

        composable(Destination.LOOKUP.route) {
            SearchScreenScaffold(
                openDrawer = openDrawer,
                onArtistClick = onArtistClick
            )
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Destination.LOOKUP_RELEASE_GROUP.route}/$releaseGroupId") {
                restoreState = true
            }
        }

        composable(
            route = "${Destination.LOOKUP_ARTIST.route}/{artistId}",
            arguments = listOf(
                navArgument("artistId") {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$artistDeeplink/{artistId}"
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
            navController.navigate("${Destination.LOOKUP_RELEASE.route}/$releaseId") {
                restoreState = true
            }
        }

        composable(
            route = "${Destination.LOOKUP_RELEASE_GROUP.route}/{releaseGroupId}",
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
            "${Destination.LOOKUP_RELEASE.route}/{releaseId}",
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

        val onHistoryItemClick: (Destination, String) -> Unit = { destination, id ->
            when (destination) {
                Destination.LOOKUP_ARTIST -> onArtistClick(id)
                Destination.LOOKUP_RELEASE_GROUP -> onReleaseGroupClick(id)
                Destination.LOOKUP_RELEASE -> onReleaseClick(id)
                else -> {
                    // Not supported.
                }
            }
        }

        composable(
            Destination.HISTORY.route
        ) {
            HistoryScreenScaffold(
                openDrawer = openDrawer,
                onItemClick = onHistoryItemClick
            )
        }
    }
}
