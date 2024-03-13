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
import ly.david.musicsearch.android.feature.nowplaying.NowPlayingHistoryScaffold
import ly.david.musicsearch.android.feature.spotify.SpotifyScaffold
import ly.david.musicsearch.core.models.common.transformThisIfNotNullOrEmpty
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.navigation.toLookupDestination
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.musicsearch.core.models.network.toMusicBrainzEntity
import ly.david.ui.common.screen.CollectionListScreen
import ly.david.ui.common.screen.CollectionScreen
import ly.david.ui.common.screen.DetailsScreen
import ly.david.ui.common.screen.HistoryScreen
import ly.david.ui.common.screen.NowPlayingHistoryScreen
import ly.david.ui.common.screen.SearchScreen
import ly.david.ui.common.screen.SettingsScreen
import ly.david.ui.common.screen.SpotifyPlayingScreen
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

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    onAddToCollectionMenuClick: (entity: MusicBrainzEntity, id: String) -> Unit,
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

        addLookupEntityScreen(
            entity = MusicBrainzEntity.AREA,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.AREA,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.ARTIST,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
//            ArtistUi(
//                artistId = entity,
//                modifier = modifier,
//                titleWithDisambiguation = title,
//                onItemClick = onLookupEntityClick,
//                onBack = navController::navigateUp,
//                onAddToCollectionMenuClick = onAddToCollectionMenuClick,
//                showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
//                onShowMoreInfoInReleaseListItemChange = onShowMoreInfoInReleaseListItemChange,
//                sortReleaseGroupListItems = sortReleaseGroupListItems,
//                onSortReleaseGroupListItemsChange = onSortReleaseGroupListItemsChange,
//            )
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.ARTIST,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.EVENT,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.EVENT,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.GENRE,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.GENRE,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.INSTRUMENT,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.INSTRUMENT,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.LABEL,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.LABEL,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.PLACE,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.PLACE,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RECORDING,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.RECORDING,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RELEASE,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.RELEASE,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.RELEASE_GROUP,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.SERIES,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.SERIES,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
            )
        }

        addLookupEntityScreen(
            entity = MusicBrainzEntity.WORK,
            uriPrefix = uriPrefix,
        ) { entityId, title ->
            val backStack = rememberSaveableBackStack(
                root = DetailsScreen(
                    entity = MusicBrainzEntity.WORK,
                    id = entityId,
                    title = title,
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: temp
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
                },
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
            val backStack = rememberSaveableBackStack(root = CollectionListScreen)
            val navigator = rememberCircuitNavigator(backStack)

            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,
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

            val backStack = rememberSaveableBackStack(
                initialScreens = listOf(
                    CollectionListScreen,
                    CollectionScreen(collectionId),
                ),
            )
            val navigator = rememberCircuitNavigator(backStack)
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
                modifier = modifier,

                // TODO: these do not work, because we're not actually going through this CircuitContent
                //  unless we deeplinked into here
                unavailableRoute = { screen, _ ->
                    when (screen) {
                        is DetailsScreen -> {
                            onLookupEntityClick(
                                screen.entity,
                                screen.id,
                                screen.title,
                            )
                        }

                        else -> {}
                    }
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
