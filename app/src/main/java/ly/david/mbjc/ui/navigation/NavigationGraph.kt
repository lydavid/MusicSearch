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
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.Destination
import ly.david.data.domain.toLookupDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.resourceUri
import ly.david.data.network.toMusicBrainzResource
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScaffold
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.collections.CollectionScaffold
import ly.david.mbjc.ui.event.EventScaffold
import ly.david.mbjc.ui.experimental.SpotifyScreen
import ly.david.mbjc.ui.genre.GenreScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScaffold
import ly.david.mbjc.ui.search.SearchScaffold
import ly.david.mbjc.ui.series.SeriesScaffold
import ly.david.mbjc.ui.work.WorkScaffold
import ly.david.ui.common.R
import ly.david.ui.history.HistoryScaffold

private const val ID = "id"
private const val TITLE = "title"

private fun String.encodeUtf8(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

private fun String.decodeUtf8(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}

internal fun NavHostController.goToResource(entity: MusicBrainzResource, id: String, title: String? = null) {
    val route = "${entity.toLookupDestination().route}/$id" +
        title?.encodeUtf8().transformThisIfNotNullOrEmpty { "?$TITLE=$it" }
    this.navigate(route)
}

internal fun NavHostController.goTo(destination: Destination) {
    val route = destination.route
    this.navigate(route)
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    deleteHistoryDelegate: ly.david.ui.history.DeleteHistoryDelegate,
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onCreateCollectionClick: () -> Unit = {},
    onAddToCollectionMenuClick: (entity: MusicBrainzResource, id: String) -> Unit = { _, _ -> },
    onDeleteFromCollection: (collectionId: String, entityId: String, name: String) -> Unit = { _, _, _ -> },
    showMoreInfoInReleaseListItem: Boolean = true,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit = {},
    sortReleaseGroupListItems: Boolean = false,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit = {},
) {
    val deeplinkSchema = stringResource(id = R.string.deeplink_schema)
    val uriPrefix = "$deeplinkSchema://app/"

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val onLookupEntityClick: (MusicBrainzResource, String, String?) -> Unit = { entity, id, title ->
            navController.goToResource(entity, id, title)
        }

        val onCollectionClick: (String, Boolean) -> Unit = { collectionId, _ ->
            navController.navigate("${Destination.COLLECTIONS.route}/$collectionId")
        }

        val searchMusicBrainz: (String, MusicBrainzResource) -> Unit = { query, type ->
            val route = Destination.LOOKUP.route + "?query=$query&type=${type.resourceUri}"
            navController.navigate(route)
        }

        composable(Destination.LOOKUP.route) {
            SearchScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick
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
                    uriPattern = "$uriPrefix${Destination.LOOKUP.route}?query={query}&type={type}"
                }
            )
        ) { entry ->
            val query = entry.arguments?.getString("query")?.decodeUtf8()
            val type = entry.arguments?.getString("type")?.toMusicBrainzResource()

            SearchScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick,
                initialQuery = query,
                initialEntity = type
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.AREA,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            AreaScaffold(
                areaId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.ARTIST,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            ArtistScaffold(
                artistId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onItemClick = onLookupEntityClick,
                onBack = navController::navigateUp,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
                sortReleaseGroupListItems = sortReleaseGroupListItems,
                onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.EVENT,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            EventScaffold(
                eventId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.GENRE,
            uriPrefix = uriPrefix
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
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            InstrumentScaffold(
                instrumentId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.LABEL,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            LabelScaffold(
                labelId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.PLACE,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            PlaceScaffold(
                placeId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RECORDING,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            RecordingScaffold(
                recordingId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            ReleaseScaffold(
                releaseId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }
        addLookupResourceScreen(
            resource = MusicBrainzResource.RELEASE_GROUP,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            ReleaseGroupScaffold(
                releaseGroupId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.SERIES,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            SeriesScaffold(
                seriesId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupResourceScreen(
            resource = MusicBrainzResource.WORK,
            uriPrefix = uriPrefix
        ) { resourceId, title ->
            WorkScaffold(
                workId = resourceId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        composable(
            Destination.HISTORY.route
        ) {
            HistoryScaffold(
                deleteHistoryDelegate = deleteHistoryDelegate,
                modifier = modifier,
                onItemClick = onLookupEntityClick
            )
        }

        composable(
            Destination.COLLECTIONS.route
        ) {
            CollectionListScaffold(
                modifier = modifier,
                onCollectionClick = onCollectionClick,
                onCreateCollectionClick = onCreateCollectionClick
            )
        }

        composable(
            route = "${Destination.COLLECTIONS.route}/{$ID}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.COLLECTIONS.route}/{$ID}"
                }
            )
        ) { entry ->
            val collectionId = entry.arguments?.getString(ID) ?: return@composable

            CollectionScaffold(
                collectionId = collectionId,
                modifier = modifier,
                onItemClick = onLookupEntityClick,
                onBack = navController::navigateUp,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
                sortReleaseGroupListItems = sortReleaseGroupListItems,
                onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange,
                onDeleteFromCollection = { collectableId, name ->
                    onDeleteFromCollection(collectionId, collectableId, name)
                }
            )
        }

        val onSettingsClick: (Destination) -> Unit = { destination ->
            when (destination) {
                Destination.EXPERIMENTAL_SPOTIFY -> {
                    navController.goTo(destination)
                }

                else -> {
                    // Nothing.
                }
            }
        }

        composable(
            Destination.SETTINGS.route
        ) {
            ly.david.ui.settings.SettingsScaffold(
                modifier = modifier,
                onDestinationClick = { destination ->
                    onSettingsClick(destination)
                },
                onLoginClick = onLoginClick,
                onLogoutClick = onLogoutClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
                sortReleaseGroupListItems = sortReleaseGroupListItems,
                onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange
            )
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
    uriPrefix: String,
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
                uriPattern = "$uriPrefix${resource.resourceUri}/{$ID}?$TITLE={$TITLE}"
            }
        )
    ) { entry: NavBackStackEntry ->
        val resourceId = entry.arguments?.getString(ID) ?: return@composable
        val title = entry.arguments?.getString(TITLE)?.decodeUtf8()
        scaffold(resourceId, title)
    }
}
