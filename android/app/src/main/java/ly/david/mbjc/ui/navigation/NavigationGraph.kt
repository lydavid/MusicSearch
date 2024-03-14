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

// TODO: don't need since we don't transform these into url anymore?
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
    modifier: Modifier = Modifier,
) {
    val uriPrefix = "$DEEP_LINK_SCHEMA://app/"

    NavHost(
        navController = navController,
        startDestination = Destination.LOOKUP.route,
    ) {

        val searchMusicBrainz: (String, MusicBrainzEntity) -> Unit = { query, type ->
            val route = Destination.LOOKUP.route +
                "?$QUERY=${query.encodeUtf8()}" +
                "&$TYPE=${type.resourceUri}"
            navController.navigate(route)
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
