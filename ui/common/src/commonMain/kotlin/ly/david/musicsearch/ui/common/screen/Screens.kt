package ly.david.musicsearch.ui.common.screen

import com.slack.circuit.runtime.screen.ParcelablePopResult
import com.slack.circuit.runtime.screen.ParcelableScreen
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
) : ParcelableScreen, NavigatableFromOverlayResult

@Parcelize
data class LookupUrlScreen(
    val query: String? = null,
) : ParcelableScreen

@Parcelize
data class ArtistCollaborationScreen(
    val id: String,
    val name: String,
) : ParcelableScreen

@Parcelize
data object DatabaseScreen : ParcelableScreen

@Parcelize
data class AllEntitiesScreen(
    val entityType: MusicBrainzEntityType,
) : ParcelableScreen

@Parcelize
data object HistoryScreen : ParcelableScreen

@Parcelize
data class CollectionListScreen(
    val newCollectionId: String? = null,
    val newCollectionName: String? = null,
    val newCollectionEntity: MusicBrainzEntityType? = null,
) : ParcelableScreen

@Parcelize
data class CollectionScreen(
    val collectionId: String,
    val collectableId: String? = null,
) : ParcelableScreen

@Parcelize
data class EditCollectionScreen(
    val entityType: MusicBrainzEntityType,
    val collectableIds: List<String>,
) : ParcelableScreen

@Parcelize
data class TagDetailsScreen(
    val entity: MusicBrainzEntity,
    val genreOrTag: GenreOrTag,
) : ParcelableScreen

@Parcelize
data class GenreOrTagScreen(
    val genreOrTag: GenreOrTag,
) : ParcelableScreen, NavigatableFromOverlayResult

@Parcelize
sealed interface NavigatableFromOverlayResult : Screen, CommonParcelable

@Parcelize
data object PopWithoutResult : PopResult, ParcelablePopResult

// TODO: rename to OverlayPopResult: isn't just for snackbars
@Parcelize
data class SnackbarPopResult<T : CommonParcelable>(
    val feedback: T?,
) : PopResult, ParcelablePopResult

@Parcelize
data class DetailsScreen(
    val entityType: MusicBrainzEntityType,
    val id: String,
) : ParcelableScreen, NavigatableFromOverlayResult

@Parcelize
data class CoverArtsScreen(
    val entity: MusicBrainzEntity? = null,
) : ParcelableScreen

@Parcelize
data class StatsScreen(
    val browseMethod: BrowseMethod,
    val tabs: ImmutableList<Tab>,
    val isRemote: Boolean = true,
) : ParcelableScreen

@Parcelize
data class ReleaseStatusesScreen(
    val browseMethod: BrowseMethod,
) : ParcelableScreen

@Parcelize
data object SettingsScreen : ParcelableScreen

@Parcelize
data object AppearanceSettingsScreen : ParcelableScreen

@Parcelize
data object BehaviourSettingsScreen : ParcelableScreen

@Parcelize
data object ImagesSettingsScreen : ParcelableScreen

@Parcelize
data object ServicesSettingsScreen : ParcelableScreen

@Parcelize
data object ListensSettingsScreen : ParcelableScreen

@Parcelize
data object LicensesScreen : ParcelableScreen

@Parcelize
data object NowPlayingHistoryScreen : ParcelableScreen

@Parcelize
data object SpotifyHistoryScreen : ParcelableScreen

@Parcelize
data class ListensScreen(
    val entityFacet: MusicBrainzEntity? = null,
    val dateTimeEpochSeconds: Long? = null,
) : ParcelableScreen

@Parcelize
data class SubmitListenScreen(
    val submitListenType: SubmitListenType,
) : ParcelableScreen
