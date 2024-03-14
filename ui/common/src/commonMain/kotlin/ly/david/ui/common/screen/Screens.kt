package ly.david.ui.common.screen

import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.ui.common.topappbar.Tab

sealed class AppScreen(
    val path: String,
) : Screen

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : AppScreen("search")

@CommonParcelize
data object HistoryScreen : AppScreen("history")

@CommonParcelize
data object CollectionListScreen : AppScreen("collections")

@CommonParcelize
data class CollectionScreen(
    val id: String,
) : AppScreen("collection")

@CommonParcelize
data class AddToCollectionScreen(
    val entity: MusicBrainzEntity,
    val id: String,
) : AppScreen("addtocollection")

@CommonParcelize
data class DetailsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val title: String?,
) : AppScreen(entity.resourceUri)

@CommonParcelize
data class StatsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val tabs: List<Tab>,
) : AppScreen("${entity.resourceUri}/stats")

@CommonParcelize
data object SettingsScreen : AppScreen("settings")

@CommonParcelize
data object LicensesScreen : AppScreen("settings/licenses")

@CommonParcelize
data object NowPlayingHistoryScreen : AppScreen("nowplayinghistory")

@CommonParcelize
data object SpotifyPlayingScreen : AppScreen("spotifyplaying")
