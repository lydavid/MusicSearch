package ly.david.mbjc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import ly.david.data.navigation.Destination
import ly.david.data.navigation.toLookupDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.toMusicBrainzResource
import ly.david.mbjc.R
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScaffold
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.collections.CollectionScaffold
import ly.david.mbjc.ui.event.EventScaffold
import ly.david.mbjc.ui.experimental.ExperimentalSettingsScaffold
import ly.david.mbjc.ui.experimental.SpotifyScreen
import ly.david.mbjc.ui.genre.GenreScaffold
import ly.david.mbjc.ui.history.HistoryScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScaffold
import ly.david.mbjc.ui.search.SearchScreenScaffold
import ly.david.mbjc.ui.series.SeriesScaffold
import ly.david.mbjc.ui.settings.SettingsScaffold
import ly.david.mbjc.ui.work.WorkScaffold
import timber.log.Timber

private const val ID = "id"
private const val TITLE = "title"

internal fun NavHostController.goToResource(destination: Destination, id: String, title: String? = null) {
    var route = "${destination.route}/$id"
    if (!title.isNullOrEmpty()) route += "?$TITLE=${URLEncoder.encode(title, StandardCharsets.UTF_8.toString())}"
    this.navigate(route)
}

internal fun NavHostController.goTo(destination: Destination) {
    val route = destination.route
    this.navigate(route)
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        // TODO: [low] should rethink this structure once we introduce more non-MB-resource destinations
        val onDestinationClick: (Destination, String, String?) -> Unit = { destination, id, title ->
            when (destination) {
                Destination.LOOKUP_AREA,
                Destination.LOOKUP_ARTIST,
                Destination.LOOKUP_EVENT,
                Destination.LOOKUP_GENRE,
                Destination.LOOKUP_INSTRUMENT,
                Destination.LOOKUP_LABEL,
                Destination.LOOKUP_PLACE,
                Destination.LOOKUP_RECORDING,
                Destination.LOOKUP_RELEASE,
                Destination.LOOKUP_RELEASE_GROUP,
                Destination.LOOKUP_SERIES,
                Destination.LOOKUP_WORK,
                Destination.COLLECTIONS_AREA,
                Destination.COLLECTIONS_ARTIST,
                Destination.COLLECTIONS_EVENT,
                Destination.COLLECTIONS_INSTRUMENT,
                Destination.COLLECTIONS_LABEL,
                Destination.COLLECTIONS_PLACE,
                Destination.COLLECTIONS_RECORDING,
                Destination.COLLECTIONS_RELEASE,
                Destination.COLLECTIONS_RELEASE_GROUP,
                Destination.COLLECTIONS_SERIES,
                Destination.COLLECTIONS_WORK -> navController.goToResource(destination, id, title)
                Destination.LOOKUP_URL -> {
                    // Expected to be handled elsewhere.
                }
                Destination.LOOKUP,
                Destination.HISTORY,
                Destination.COLLECTIONS,
                Destination.SETTINGS -> {
                    // Not handled.
                }
                Destination.EXPERIMENTAL_SETTINGS,
                Destination.EXPERIMENTAL_SPOTIFY -> {
                    navController.goTo(destination)
                }
            }
        }

        val onCollectionClick: (String) -> Unit = { collectionId ->
            navController.navigate("${Destination.COLLECTIONS.route}/$collectionId")
        }

        val searchMusicBrainz: (String, MusicBrainzResource) -> Unit = { query, type ->
            val route = Destination.LOOKUP.route + "?query=${query}&type=${type.resourceName}"
            Timber.d("1. $query")
            navController.navigate(route)
        }

        composable(Destination.LOOKUP.route) {
            SearchScreenScaffold(
                modifier = modifier,
                onItemClick = onDestinationClick
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

            Timber.d("2. $query")

            SearchScreenScaffold(
                modifier = modifier,
                onItemClick = onDestinationClick,
                searchQuery = query,
                searchOption = type
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.AREA,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            AreaScaffold(
                areaId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.ARTIST,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ArtistScaffold(
                artistId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onItemClick = onDestinationClick,
                onBack = navController::navigateUp,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.EVENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            EventScaffold(
                eventId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.GENRE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            GenreScaffold(
                genreId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.INSTRUMENT,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            InstrumentScaffold(
                instrumentId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.LABEL,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            LabelScaffold(
                labelId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.PLACE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            PlaceScaffold(
                placeId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RECORDING,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            RecordingScaffold(
                recordingId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseScaffold(
                releaseId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick,
            )
        }
        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE_GROUP,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            ReleaseGroupScaffold(
                releaseGroupId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.SERIES,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            SeriesScaffold(
                seriesId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.WORK,
            deeplinkSchema = deeplinkSchema
        ) { resourceId, title ->
            WorkScaffold(
                workId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onDestinationClick
            )
        }

        composable(
            Destination.HISTORY.route
        ) {
            HistoryScaffold(
                modifier = modifier,
                onItemClick = onDestinationClick
            )
        }

        composable(
            Destination.SETTINGS.route
        ) {
            SettingsScaffold(
                modifier = modifier,
                onDestinationClick = { destination ->
                    onDestinationClick(destination, "", null)
                }
            )
        }

        composable(
            Destination.COLLECTIONS.route
        ) {
            CollectionListScaffold(
                modifier = modifier,
                onCollectionClick = onCollectionClick
            )
        }

        composable(
            route = "${Destination.COLLECTIONS.route}/{$ID}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$deeplinkSchema://${Destination.COLLECTIONS.route}/{$ID}"
                }
            )
        ) { entry ->
            val collectionId = entry.arguments?.getString(ID) ?: return@composable

            CollectionScaffold(
                collectionId = collectionId,
                modifier = modifier,
                onDestinationClick = { destination, id ->
                    onDestinationClick(destination, id, null)
                }
            )
        }

        composable(
            Destination.EXPERIMENTAL_SETTINGS.route
        ) {
            ExperimentalSettingsScaffold()
        }

        composable(
            Destination.EXPERIMENTAL_SPOTIFY.route
        ) {
            SpotifyScreen(
                searchMusicBrainz = searchMusicBrainz
            )
        }
    }
}

private fun NavGraphBuilder.addLookupResourceScreen(
    resource: MusicBrainzResource,
    deeplinkSchema: String,
    scaffold: @Composable (resourceId: String, titleWithDisambiguation: String?) -> Unit
) {
    composable(
        route = "${resource.toLookupDestination().route}/{$ID}?$TITLE={$TITLE}",
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
