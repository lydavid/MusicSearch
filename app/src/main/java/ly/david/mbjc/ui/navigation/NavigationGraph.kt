package ly.david.mbjc.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import ly.david.data.navigation.Destination
import ly.david.data.navigation.toDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.toMusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScaffold
import ly.david.mbjc.ui.event.EventScaffold
import ly.david.mbjc.ui.experimental.ExperimentalScreen
import ly.david.mbjc.ui.genre.GenreScaffold
import ly.david.mbjc.ui.history.HistoryScreenScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScaffold
import ly.david.mbjc.ui.search.SearchScreenScaffold
import ly.david.mbjc.ui.work.WorkScaffold

private const val ID = "id"
private const val TITLE = "title"

internal fun NavHostController.goTo(destination: Destination, id: String, title: String? = null) {
    var route = "${destination.route}/$id"
    if (!title.isNullOrEmpty()) route += "?$TITLE=$title"
    this.navigate(route)
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    openDrawer: () -> Unit = {}
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val onReleaseGroupClick: (String) -> Unit = { releaseGroupId ->
            navController.navigate("${Destination.LOOKUP_RELEASE_GROUP.route}/$releaseGroupId")
        }

        val onLookupItemClick: (Destination, String, String?) -> Unit = { destination, id, title ->
            when (destination) {
                Destination.LOOKUP_ARTIST,
                Destination.LOOKUP_RELEASE_GROUP,
                Destination.LOOKUP_RELEASE,
                Destination.LOOKUP_RECORDING,
                Destination.LOOKUP_AREA,
                Destination.LOOKUP_PLACE,
                Destination.LOOKUP_INSTRUMENT,
                Destination.LOOKUP_LABEL,
                Destination.LOOKUP_WORK,
                Destination.LOOKUP_EVENT,
                Destination.LOOKUP_SERIES,
                Destination.LOOKUP_GENRE -> navController.goTo(destination, id, title)

                Destination.LOOKUP_URL -> {
                    // Expected to be handled elsewhere.
                }

                Destination.LOOKUP -> {
                    // Not handled.
                }
                Destination.HISTORY -> {
                    // Not handled.
                }
                Destination.EXPERIMENTAL -> {
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

        addResourceScreen(
            resource = MusicBrainzResource.AREA,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            AreaScaffold(
                areaId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.ARTIST,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ArtistScaffold(
                artistId = resourceId,
                titleWithDisambiguation = title,
                onReleaseGroupClick = onReleaseGroupClick,
                onItemClick = onLookupItemClick,
                onBack = navController::navigateUp,
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.EVENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            EventScaffold(
                eventId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.GENRE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            GenreScaffold(
                genreId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.INSTRUMENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            InstrumentScaffold(
                instrumentId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.LABEL,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            LabelScaffold(
                labelId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.PLACE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            PlaceScaffold(
                placeId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.RECORDING,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            RecordingScaffold(
                recordingId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick,
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.RELEASE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseScaffold(
                releaseId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick,
            )
        }
        addResourceScreen(
            resource = MusicBrainzResource.RELEASE_GROUP,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseGroupScaffold(
                releaseGroupId = resourceId,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupItemClick
            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.SERIES,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            // TODO:
            Text(text = resourceId)
            //            SeriesScaffold(
//                eventId = seriesId,
//                onBack = navController::navigateUp,
//                onItemClick = onLookupItemClick
//            )
        }

        addResourceScreen(
            resource = MusicBrainzResource.WORK,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            WorkScaffold(
                workId = resourceId,
                titleWithDisambiguation = title,
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

        composable(
            Destination.EXPERIMENTAL.route
        ) {
            ExperimentalScreen()
        }
    }
}

private fun NavGraphBuilder.addResourceScreen(
    resource: MusicBrainzResource,
    deeplinkSchema: String,
    scaffold: @Composable (resourceId: String, titleWithDisambiguation: String?) -> Unit
) {
    composable(
        "${resource.toDestination().route}/{$ID}?$TITLE={$TITLE}",
        arguments = listOf(
            navArgument(ID) {
                type = NavType.StringType // Make argument type safe
            },
            navArgument(TITLE) {
                nullable = true
                defaultValue = null
                type = NavType.StringType
            },
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "$deeplinkSchema://${resource.resourceName}/{$ID}?$TITLE={$TITLE}"
            }
        )
    ) { entry: NavBackStackEntry ->
        val resourceId = entry.arguments?.getString(ID) ?: return@composable
        val title = entry.arguments?.getString(TITLE)
        scaffold(resourceId, title)
    }
}
