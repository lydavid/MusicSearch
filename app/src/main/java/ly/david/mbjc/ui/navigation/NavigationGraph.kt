package ly.david.mbjc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.mbjc.R
import ly.david.mbjc.ui.artist.ArtistScreenScaffold
import ly.david.mbjc.ui.history.HistoryScreenScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScreenScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScreenScaffold
import ly.david.mbjc.ui.search.SearchScreenScaffold

private const val RECORDING_ID = "recordingId"

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)
    val artistDeeplink = stringResource(id = R.string.deeplink_artist)
    val releaseGroupDeeplink = stringResource(id = R.string.deeplink_release_group)
    val releaseDeeplink = stringResource(id = R.string.deeplink_release)
    val recordingDeeplink = stringResource(id = R.string.deeplink_recording)

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val onArtistClick: (String) -> Unit = { artistId ->
            // TODO: these should be built by Destination
            navController.navigate("${Destination.LOOKUP_ARTIST.route}/$artistId") {
                restoreState = true
            }
        }

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Destination.LOOKUP_RELEASE_GROUP.route}/$releaseGroupId") {
                restoreState = true
            }
        }

        val onReleaseClick: (String) -> Unit = { releaseId ->
            navController.navigate("${Destination.LOOKUP_RELEASE.route}/$releaseId") {
                restoreState = true
            }
        }

        val onRecordingClick: (String) -> Unit = { recordingId ->
            navController.navigate("${Destination.LOOKUP_RECORDING.route}/$recordingId") {
                restoreState = true
            }
        }

        val onLookupItemClick: (Destination, String) -> Unit = { destination, id ->
            when (destination) {
                Destination.LOOKUP_ARTIST -> onArtistClick(id)
                Destination.LOOKUP_RELEASE_GROUP -> onReleaseGroupClick(id)
                Destination.LOOKUP_RELEASE -> onReleaseClick(id)
                Destination.LOOKUP_RECORDING -> onRecordingClick(id)
                // TODO: place, work, label

                // TODO: support rest
                else -> {
                    // Not supported.
                }
            }
        }

        composable(Destination.LOOKUP.route) {
            SearchScreenScaffold(
                openDrawer = openDrawer,
                onItemClick = onLookupItemClick
            )
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
                onBack = navController::navigateUp
            )
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
                onBack = navController::navigateUp
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
                onBack = navController::navigateUp,
                onRecordingClick = onRecordingClick
            )
        }

        composable(
            "${Destination.LOOKUP_RECORDING.route}/{$RECORDING_ID}",
            arguments = listOf(
                navArgument(RECORDING_ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$recordingDeeplink/{$RECORDING_ID}"
                }
            )
        ) { entry ->
            val recordingId = entry.arguments?.getString(RECORDING_ID) ?: return@composable
            RecordingScaffold(
                recordingId = recordingId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            Destination.HISTORY.route
        ) {
            HistoryScreenScaffold(
                openDrawer = openDrawer,
                onItemClick = onLookupItemClick
            )
        }
    }
}
