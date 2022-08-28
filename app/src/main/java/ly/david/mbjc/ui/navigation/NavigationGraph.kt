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
import ly.david.mbjc.data.network.toMusicBrainzResource
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScreenScaffold
import ly.david.mbjc.ui.history.HistoryScreenScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScreenScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScreenScaffold
import ly.david.mbjc.ui.search.SearchScreenScaffold
import ly.david.mbjc.ui.work.WorkScaffold

private const val ID = "id"

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
    val workDeeplink = stringResource(id = R.string.deeplink_work)

    val areaDeeplink = stringResource(id = R.string.deeplink_area)
    val placeDeeplink = stringResource(id = R.string.deeplink_place)

    val instrumentDeeplink = stringResource(id = R.string.deeplink_instrument)
    val labelDeeplink = stringResource(id = R.string.deeplink_label)

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

        val onAreaClick: (String) -> Unit = { recordingId ->
            navController.navigate("${Destination.LOOKUP_AREA.route}/$recordingId") {
                restoreState = true
            }
        }

        val onPlaceClick: (String) -> Unit = { placeId ->
            navController.navigate("${Destination.LOOKUP_PLACE.route}/$placeId") {
                restoreState = true
            }
        }

        val onInstrumentClick: (String) -> Unit = { instrumentId ->
            navController.navigate("${Destination.LOOKUP_INSTRUMENT.route}/$instrumentId") {
                restoreState = true
            }
        }

        val onLabelClick: (String) -> Unit = { labelId ->
            navController.navigate("${Destination.LOOKUP_LABEL.route}/$labelId") {
                restoreState = true
            }
        }

        val onWorkClick: (String) -> Unit = { workId ->
            navController.navigate("${Destination.LOOKUP_WORK.route}/$workId") {
                restoreState = true
            }
        }

        val onLookupItemClick: (Destination, String) -> Unit = { destination, id ->
            when (destination) {
                Destination.LOOKUP_ARTIST -> onArtistClick(id)
                Destination.LOOKUP_RELEASE_GROUP -> onReleaseGroupClick(id)
                Destination.LOOKUP_RELEASE -> onReleaseClick(id)
                Destination.LOOKUP_RECORDING -> onRecordingClick(id)
                Destination.LOOKUP_AREA -> onAreaClick(id)
                Destination.LOOKUP_PLACE -> onPlaceClick(id)
                Destination.LOOKUP_INSTRUMENT -> onInstrumentClick(id)
                Destination.LOOKUP_LABEL -> onLabelClick(id)
                Destination.LOOKUP_WORK -> onWorkClick(id)

                Destination.LOOKUP_EVENT -> TODO()
                Destination.LOOKUP_SERIES -> TODO()

                Destination.LOOKUP_GENRE -> TODO()

                Destination.LOOKUP_URL -> {
                    // Expected to be handled elsewhere.
                }

                Destination.LOOKUP -> {
                    // Not handled.
                }
                Destination.HISTORY -> {
                    // Not handled.
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
            route = "${Destination.LOOKUP.route}?query={query}&type={type}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                },
                navArgument("type") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://${Destination.LOOKUP.route}?query={query}&type={type}"
                }
            )
        ) { entry ->
            val query = entry.arguments?.getString("query")
            val type = entry.arguments?.getString("type")?.toMusicBrainzResource()

            SearchScreenScaffold(
                openDrawer = openDrawer,
                onItemClick = onLookupItemClick,
                searchQuery = query,
                searchOption = type
            )
        }

        composable(
            route = "${Destination.LOOKUP_ARTIST.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$artistDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val artistId = entry.arguments?.getString(ID) ?: return@composable
            ArtistScreenScaffold(
                artistId = artistId,
                onReleaseGroupClick = onReleaseGroupClick,
                onItemClick = onLookupItemClick,
                onBack = navController::navigateUp,
            )
        }

        composable(
            route = "${Destination.LOOKUP_RELEASE_GROUP.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release-group/81d75493-78b6-4a37-b5ae-2a3918ee3756" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$releaseGroupDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val releaseGroupId = entry.arguments?.getString(ID) ?: return@composable
            ReleaseGroupScreenScaffold(
                releaseGroupId = releaseGroupId,
                onReleaseClick = onReleaseClick,
                onBack = navController::navigateUp
            )
        }

        composable(
            "${Destination.LOOKUP_RELEASE.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            // Example: adb shell am start -d "mbjc://release/165f6643-2edb-4795-9abe-26bd0533e59d" -a android.intent.action.VIEW
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$releaseDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val releaseId = entry.arguments?.getString(ID) ?: return@composable
            ReleaseScreenScaffold(
                releaseId = releaseId,
                onBack = navController::navigateUp,
                onRecordingClick = onRecordingClick
            )
        }

        composable(
            "${Destination.LOOKUP_RECORDING.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$recordingDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val recordingId = entry.arguments?.getString(ID) ?: return@composable
            RecordingScaffold(
                recordingId = recordingId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            "${Destination.LOOKUP_WORK.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$workDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val workId = entry.arguments?.getString(ID) ?: return@composable
            WorkScaffold(
                workId = workId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            "${Destination.LOOKUP_AREA.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$areaDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val areaId = entry.arguments?.getString(ID) ?: return@composable
            AreaScaffold(
                areaId = areaId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            "${Destination.LOOKUP_PLACE.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$placeDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val placeId = entry.arguments?.getString(ID) ?: return@composable
            PlaceScaffold(
                placeId = placeId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            "${Destination.LOOKUP_INSTRUMENT.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType // Make argument type safe
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$instrumentDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val instrumentId = entry.arguments?.getString(ID) ?: return@composable
            InstrumentScaffold(
                instrumentId = instrumentId,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        composable(
            "${Destination.LOOKUP_LABEL.route}/{$ID}",
            arguments = listOf(
                navArgument(ID) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://$labelDeeplink/{$ID}"
                }
            )
        ) { entry ->
            val labelId = entry.arguments?.getString(ID) ?: return@composable
            LabelScaffold(
                labelId = labelId,
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
