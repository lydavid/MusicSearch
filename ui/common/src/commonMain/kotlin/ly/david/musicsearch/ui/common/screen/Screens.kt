package ly.david.musicsearch.ui.common.screen

import com.slack.circuit.runtime.screen.Screen
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.topappbar.Tab

@Parcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntity? = null,
) : Screen

@Parcelize
data object GraphScreen : Screen

@Parcelize
data object HistoryScreen : Screen

@Parcelize
data object CollectionListScreen : Screen

@Parcelize
data class CollectionScreen(
    val id: String,
) : Screen

@Parcelize
data class AddToCollectionScreen(
    val entity: MusicBrainzEntity,
    val id: String,
) : Screen

@Parcelize
data class DetailsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val title: String?,
) : Screen

@Parcelize
data class CoverArtsScreen(
    val id: String,
) : Screen

@Parcelize
data class StatsScreen(
    val entity: MusicBrainzEntity,
    val id: String,
    val tabs: List<Tab>,
) : Screen

@Parcelize
data object SettingsScreen : Screen

@Parcelize
data object LicensesScreen : Screen

@Parcelize
data object NowPlayingHistoryScreen : Screen

@Parcelize
data object SpotifyHistoryScreen : Screen
