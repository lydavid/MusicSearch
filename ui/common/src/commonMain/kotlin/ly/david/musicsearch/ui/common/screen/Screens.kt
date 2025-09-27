package ly.david.musicsearch.ui.common.screen

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.parcelize.Parcelize
import ly.david.musicsearch.ui.common.topappbar.Tab

@Parcelize
data class SearchScreen(
    val query: String? = null,
    val entity: MusicBrainzEntityType? = null,
) : Screen

@Parcelize
data class ArtistCollaborationScreen(
    val id: String,
    val name: String,
) : Screen

@Parcelize
data object DatabaseScreen : Screen

@Parcelize
data class AllEntitiesScreen(
    val entity: MusicBrainzEntityType,
) : Screen

@Parcelize
data object HistoryScreen : Screen

@Parcelize
data class CollectionListScreen(
    val newCollectionId: String? = null,
    val newCollectionName: String? = null,
    val newCollectionEntity: MusicBrainzEntityType? = null,
) : Screen

@Parcelize
data class CollectionScreen(
    val collectionId: String,
    val collectableId: String? = null,
) : Screen

@Parcelize
data class AddToCollectionScreen(
    val entity: MusicBrainzEntityType,
    val collectableIds: Set<String>,
) : Screen

@Parcelize
data class SnackbarPopResult(
    val message: String = "",
    val actionLabel: String? = null,
) : PopResult

@Parcelize
data class DetailsScreen(
    val entity: MusicBrainzEntityType,
    val id: String,
) : Screen

@Parcelize
data class CoverArtsScreen(
    val id: String? = null,
    val entity: MusicBrainzEntityType? = null,
) : Screen

@Parcelize
data class StatsScreen(
    val browseMethod: BrowseMethod,
    val tabs: ImmutableList<Tab>,
    val isRemote: Boolean = true,
) : Screen

@Parcelize
data object SettingsScreen : Screen

@Parcelize
data object AppearanceSettingsScreen : Screen

@Parcelize
data object ImagesSettingsScreen : Screen

@Parcelize
data object LicensesScreen : Screen

@Parcelize
data object NowPlayingHistoryScreen : Screen

@Parcelize
data object SpotifyHistoryScreen : Screen

@Parcelize
data class ListensScreen(
    val entityFacet: MusicBrainzEntity? = null,
) : Screen
