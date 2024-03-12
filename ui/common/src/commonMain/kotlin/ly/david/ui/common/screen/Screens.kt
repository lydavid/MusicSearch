package ly.david.ui.common.screen

import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.topappbar.Tab

@CommonParcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen

@CommonParcelize
data object HistoryScreen : Screen

@CommonParcelize
data object CollectionListScreen : Screen

@CommonParcelize
data class CollectionScreen(
    val id: String,
) : Screen

@CommonParcelize
data class DetailsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val title: String?,
) : Screen

@CommonParcelize
data class AreaStatsScreen(
    val id: String,
    val tabs: List<Tab>,
) : Screen

@CommonParcelize
data object SettingsScreen : Screen

@CommonParcelize
data object LicensesScreen : Screen

@CommonParcelize
data object NowPlayingHistoryScreen : Screen

@CommonParcelize
data object SpotifyPlayingScreen : Screen
