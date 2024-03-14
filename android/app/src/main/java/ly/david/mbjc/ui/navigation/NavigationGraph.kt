package ly.david.mbjc.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ly.david.musicsearch.core.models.navigation.Destination
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private const val QUERY = "query"
private const val TYPE = "type"

// TODO: don't need since we don't transform these into url anymore?
private fun String.encodeUtf8(): String {
    return URLEncoder.encode(
        this,
        StandardCharsets.UTF_8.toString(),
    )
}

@Composable
internal fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
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

//        composable(
//            Destination.SETTINGS_NOWPLAYING.route,
//        ) {
//            NowPlayingHistoryScaffold(
//                modifier = modifier,
//                onBack = navController::navigateUp,
//                searchMusicBrainz = searchMusicBrainz,
//            )
//        }

//        composable(
//            Destination.EXPERIMENTAL_SPOTIFY.route,
//        ) {
//            SpotifyUi(
//                modifier = modifier,
//                onBack = navController::navigateUp,
//                searchMusicBrainz = searchMusicBrainz,
//            )
//        }
    }
}
