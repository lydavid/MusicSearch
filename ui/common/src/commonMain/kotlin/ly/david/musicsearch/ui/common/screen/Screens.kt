package ly.david.musicsearch.ui.common.screen

import com.slack.circuit.runtime.screen.PopResult
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.ui.common.topappbar.Tab

@Parcelize
data class SearchScreen(
    val query: String? = null,
    val entityType: MusicBrainzEntityType? = null,
) : Screen, NavigatableFromOverlayResult

@Parcelize
data class LookupUrlScreen(
    val query: String? = null,
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
    val entityType: MusicBrainzEntityType,
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
data class EditCollectionScreen(
    val entityType: MusicBrainzEntityType,
    val collectableIds: List<String>,
) : Screen

@Parcelize
data class TagDetailsScreen(
    val entity: MusicBrainzEntity,
    val genreOrTag: GenreOrTag,
) : Screen

@Parcelize
sealed interface NavigatableFromOverlayResult : Screen, CommonParcelable

@Parcelize
data object PopWithoutResult : PopResult

// TODO: rename to OverlayPopResult: isn't just for snackbars
@Parcelize
data class SnackbarPopResult<T : CommonParcelable>(
    val feedback: T?,
) : PopResult

@Parcelize
data class DetailsScreen(
    val entityType: MusicBrainzEntityType,
    val id: String,
) : Screen, NavigatableFromOverlayResult

@Parcelize
data class CoverArtsScreen(
    val entity: MusicBrainzEntity? = null,
) : Screen

@Parcelize
data class StatsScreen(
    val browseMethod: BrowseMethod,
    val tabs: ImmutableList<Tab>,
    val isRemote: Boolean = true,
) : Screen

@Parcelize
data class ReleaseStatusesScreen(
    val browseMethod: BrowseMethod,
) : Screen

@Parcelize
data object SettingsScreen : Screen

@Parcelize
data object AppearanceSettingsScreen : Screen

@Parcelize
data object ImagesSettingsScreen : Screen

@Parcelize
data object ServicesSettingsScreen : Screen

@Parcelize
data object ListensSettingsScreen : Screen

@Parcelize
data object LicensesScreen : Screen

@Parcelize
data object NowPlayingHistoryScreen : Screen

@Parcelize
data object SpotifyHistoryScreen : Screen

@Parcelize
data class ListensScreen(
    val entityFacet: MusicBrainzEntity? = null,
    val dateTimeEpochSeconds: Long? = null,
) : Screen

@Parcelize
data class SubmitListenScreen(
    val submitListenType: SubmitListenType,
) : Screen
