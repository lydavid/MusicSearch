package ly.david.mbjc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import ly.david.mbjc.DEEP_LINK_SCHEMA
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.artist.ArtistScaffold
import ly.david.mbjc.ui.event.EventScaffold
import ly.david.mbjc.ui.genre.GenreScaffold
import ly.david.mbjc.ui.instrument.InstrumentScaffold
import ly.david.mbjc.ui.label.LabelScaffold
import ly.david.mbjc.ui.place.PlaceScaffold
import ly.david.mbjc.ui.recording.RecordingScaffold
import ly.david.mbjc.ui.release.ReleaseScaffold
import ly.david.mbjc.ui.releasegroup.ReleaseGroupScaffold
import ly.david.mbjc.ui.series.SeriesScaffold
import ly.david.mbjc.ui.work.WorkScaffold
import ly.david.musicsearch.android.feature.nowplaying.NowPlayingHistoryScaffold
import ly.david.musicsearch.android.feature.spotify.SpotifyScaffold
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.navigation.toLookupDestination
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.musicsearch.core.models.network.toMusicBrainzEntity
import ly.david.musicsearch.shared.screens.DetailsScreen
import ly.david.musicsearch.shared.screens.HistoryScreen
import ly.david.musicsearch.shared.screens.NowPlayingHistoryScreen
import ly.david.musicsearch.shared.screens.SearchScreen
import ly.david.musicsearch.shared.screens.SettingsScreen
import ly.david.musicsearch.shared.screens.SpotifyPlayingScreen
import ly.david.musicsearch.shared.feature.collections.CollectionScaffold
import ly.david.musicsearch.shared.screens.CollectionListScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val ID = "id"
private const val TITLE = "title"
private const val QUERY = "query"
private const val TYPE = "type"

private fun String.encodeUtf8(): String {
    return URLEncoder.encode(
        this,
        StandardCharsets.UTF_8.toString(),
    )
}

private fun String.decodeUtf8(): String {
    return URLDecoder.decode(
        this,
        StandardCharsets.UTF_8.toString(),
    )
}

internal fun NavHostController.goToEntityScreen(
    entity: MusicBrainzEntity,
    id: String,
    title: String? = null,
) {
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
    onCreateCollectionClick: () -> Unit,
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit,
    onDeleteFromCollection: (collectionId: String, entityId: String, name: String) -> Unit,
    showMoreInfoInReleaseListItem: Boolean,
    onShowMoreInfoInReleaseListItemChange: (Boolean) -> Unit,
    sortReleaseGroupListItems: Boolean,
    onSortReleaseGroupListItemsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uriPrefix = "$DEEP_LINK_SCHEMA://app/"

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {
        val onLookupEntityClick: (MusicBrainzEntity, String, String?) -> Unit = { entity, id, title ->
            navController.goToEntityScreen(
                entity,
                id,
                title,
            )
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
            CircuitContent(
                screen = SearchScreen(),
                modifier = modifier,
                onNavEvent = { event ->
                    when (event) {
                        is NavEvent.GoTo -> {
                            val screen = event.screen
                            if (screen is DetailsScreen) {
                                onLookupEntityClick(
                                    screen.entity,
                                    screen.id,
                                    screen.title,
                                )
                            }
                        }

                        is NavEvent.Pop -> TODO()
                        is NavEvent.ResetRoot -> TODO()
                    }
                },
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
                },
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.LOOKUP.route}?$QUERY={query}&$TYPE={type}"
                },
            ),
        ) { entry ->
            val query = entry.arguments?.getString(QUERY)?.decodeUtf8()
            val type = entry.arguments?.getString(TYPE)?.toMusicBrainzEntity()

            CircuitContent(
                screen = SearchScreen(
                    query = query,
                    entity = type,
                ),
                modifier = modifier,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.AREA,
            uriPrefix = uriPrefix,
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
            uriPrefix = uriPrefix,
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
                onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.EVENT,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            EventScaffold(
                eventId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.GENRE,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            GenreScaffold(
                genreId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.INSTRUMENT,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            InstrumentScaffold(
                instrumentId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.LABEL,
            uriPrefix = uriPrefix,
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
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            PlaceScaffold(
                placeId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RECORDING,
            uriPrefix = uriPrefix,
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
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            ReleaseScaffold(
                releaseId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }
        addLookupEntityScreen(
            entity = MusicBrainzEntity.RELEASE_GROUP,
            uriPrefix = uriPrefix,
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
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            SeriesScaffold(
                seriesId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.WORK,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            WorkScaffold(
                workId = entityId,
                modifier = modifier,
                titleWithDisambiguation = title,
                onBack = navController::navigateUp,
                onItemClick = onLookupEntityClick,
                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
            )
        }

        composable(
            route = Destination.HISTORY.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.HISTORY.route}"
                },
            ),
        ) {
            CircuitContent(
                screen = HistoryScreen,
                modifier = modifier,
                onNavEvent = { event ->
                    when (event) {
                        is NavEvent.GoTo -> {
                            val screen = event.screen
                            if (screen is DetailsScreen) {
                                onLookupEntityClick(
                                    screen.entity,
                                    screen.id,
                                    screen.title,
                                )
                            }
                        }

                        is NavEvent.Pop -> TODO()
                        is NavEvent.ResetRoot -> TODO()
                    }
                },
            )
        }

        composable(
            route = Destination.COLLECTIONS.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.COLLECTIONS.route}"
                },
            ),
        ) {
//            CollectionList(
//                modifier = modifier,
//                onCollectionClick = onCollectionClick,
//                onCreateCollectionClick = onCreateCollectionClick,
//            )
            CircuitContent(
                screen = CollectionListScreen,
                modifier = modifier,
//                onNavEvent = { event ->
//                    when (event) {
//                        is NavEvent.GoTo -> {
//                            val screen = event.screen
//                            if (screen is DetailsScreen) {
//                                onLookupEntityClick(
//                                    screen.entity,
//                                    screen.id,
//                                    screen.title,
//                                )
//                            }
//                        }
//
//                        is NavEvent.Pop -> TODO()
//                        is NavEvent.ResetRoot -> TODO()
//                    }
//                },
            )
        }

        composable(
            route = "${Destination.COLLECTIONS.route}/{$ID}",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.COLLECTIONS.route}/{$ID}"
                },
            ),
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
                    onDeleteFromCollection(
                        collectionId,
                        collectableId,
                        name,
                    )
                },
            )
        }

        composable(
            Destination.SETTINGS.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$uriPrefix${Destination.SETTINGS.route}"
                },
            ),
        ) {
            val backStack = rememberSaveableBackStack(root = SettingsScreen)
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,
                unavailableRoute = { screen, nestedModifier ->
                    when (screen) {
                        is NowPlayingHistoryScreen -> {
                            NowPlayingHistoryScaffold(
                                modifier = nestedModifier,
                                onBack = navigator::pop,
                                searchMusicBrainz = searchMusicBrainz,
                            )
                        }

                        is SpotifyPlayingScreen -> {
                            SpotifyScaffold(
                                modifier = nestedModifier,
                                onBack = navigator::pop,
                                searchMusicBrainz = searchMusicBrainz,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        composable(
            Destination.SETTINGS_NOWPLAYING.route,
        ) {
            NowPlayingHistoryScaffold(
                modifier = modifier,
                onBack = navController::navigateUp,
                searchMusicBrainz = searchMusicBrainz,
            )
        }

        composable(
            Destination.EXPERIMENTAL_SPOTIFY.route,
        ) {
            SpotifyScaffold(
                modifier = modifier,
                onBack = navController::navigateUp,
                searchMusicBrainz = searchMusicBrainz,
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
            },
        ),
    ) { entry: NavBackStackEntry ->
        val entityId = entry.arguments?.getString(ID) ?: return@composable
        val title = entry.arguments?.getString(TITLE)?.decodeUtf8()
        scaffold(
            entityId,
            title,
        )
    }
}
