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
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.resourceUri
import ly.david.data.network.toMusicBrainzEntity
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
import ly.david.ui.nowplaying.NowPlayingHistoryScaffold
import ly.david.ui.settings.SettingsScaffold
import ly.david.ui.settings.licenses.LicensesScaffold

private const val ID = "id"
private const val TITLE = "title"
private const val QUERY = "query"
private const val TYPE = "type"

private fun String.encodeUtf8(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

private fun String.decodeUtf8(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}

internal fun NavHostController.goToEntityScreen(entity: MusicBrainzEntity, id: String, title: String? = null) {
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
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit = { _, _ -> },
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
        val onLookupEntityClick: (MusicBrainzEntity, String, String?) -> Unit = { entity, id, title ->
            navController.goToEntityScreen(entity, id, title)
        }

        val onCollectionClick: (String, Boolean) -> Unit = { collectionId, _ ->
            navController.navigate("${Destination.COLLECTIONS.route}/$collectionId")
        }

        val searchMusicBrainz: (String, MusicBrainzEntity) -> Unit = { query, type ->
            val route = Destination.LOOKUP.route +
                "?$QUERY=${query.encodeUtf8()}" +
                "&$TYPE=${type.resourceUri}"
            navController.navigate(route)
        }

        composable(Destination.LOOKUP.route) {
            SearchScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick,
            )
        }

        composable(
            route = "${Destination.LOOKUP.route}?$QUERY={query}&$TYPE={type}",
            arguments = listOf(
                navArgument(QUERY) {
                    type = NavType.StringType
                },
                navArgument(TYPE) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.LOOKUP.route}?$QUERY={query}&$TYPE={type}"
                }
            )
        ) { entry ->
            val query = entry.arguments?.getString(QUERY)?.decodeUtf8()
            val type = entry.arguments?.getString(TYPE)?.toMusicBrainzEntity()

            SearchScaffold(
                modifier = modifier,
                onItemClick = onLookupEntityClick,
                initialQuery = query,
                initialEntity = type
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.AREA,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            AreaScaffold(
                areaId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.ARTIST,
            uriPrefix = uriPrefix
        ) { entity, title ->
            ArtistScaffold(
                artistId = entity,
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

        addLookupEntityScreen(
            entity = MusicBrainzEntity.EVENT,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            EventScaffold(
                eventId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.GENRE,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            GenreScaffold(
                genreId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.INSTRUMENT,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            InstrumentScaffold(
                instrumentId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.LABEL,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            LabelScaffold(
                labelId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.PLACE,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            PlaceScaffold(
                placeId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RECORDING,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            RecordingScaffold(
                recordingId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RELEASE,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            ReleaseScaffold(
                releaseId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }
        addLookupEntityScreen(
            entity = MusicBrainzEntity.RELEASE_GROUP,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            ReleaseGroupScaffold(
                releaseGroupId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.SERIES,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            SeriesScaffold(
                seriesId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.WORK,
            uriPrefix = uriPrefix
        ) { entityId, title ->
            WorkScaffold(
                workId = entityId,
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
            navController.goTo(destination)
        }

        composable(
            Destination.SETTINGS.route
        ) {
            SettingsScaffold(
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
            Destination.SETTINGS_NOWPLAYING.route
        ) {
            NowPlayingHistoryScaffold(
                modifier = modifier,
                onBack = navController::navigateUp,
                searchMusicBrainz = searchMusicBrainz,
            )
        }

        composable(
            Destination.SETTINGS_LICENSES.route
        ) {
            LicensesScaffold(
                modifier = modifier,
                onBack = navController::navigateUp
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

private fun NavGraphBuilder.addLookupEntityScreen(
    entity: MusicBrainzEntity,
    uriPrefix: String,
    scaffold: @Composable (entityId: String, titleWithDisambiguation: String?) -> Unit,
) {
    composable(
        route = "${entity.toLookupDestination().route}/{$ID}?$TITLE={$TITLE}",
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
                uriPattern = "$uriPrefix${entity.resourceUri}/{$ID}?$TITLE={$TITLE}"
            }
        )
    ) { entry: NavBackStackEntry ->
        val entityId = entry.arguments?.getString(ID) ?: return@composable
        val title = entry.arguments?.getString(TITLE)?.decodeUtf8()
        scaffold(entityId, title)
    }
}
