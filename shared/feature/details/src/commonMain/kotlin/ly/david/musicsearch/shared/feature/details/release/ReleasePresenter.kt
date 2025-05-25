package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.track.TracksByEntityUiEvent
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState

internal class ReleasePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ReleaseRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val tracksByReleasePresenter: TracksByReleasePresenter,
    private val artistsListPresenter: ArtistsListPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<ReleaseUiState> {

    @Composable
    override fun present(): ReleaseUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var release: ReleaseDetailsModel? by rememberRetained { mutableStateOf(null) }
        var numberOfImages: Int? by rememberSaveable { mutableStateOf(null) }
        val tabs: ImmutableList<ReleaseTab> = ReleaseTab.entries.toPersistentList()
        var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }
        var isReleaseEventsCollapsed by rememberSaveable { mutableStateOf(false) }

        val tracksByReleaseUiState = tracksByReleasePresenter.present()
        val tracksEventSink = tracksByReleaseUiState.eventSink

        val artistsByEntityUiState = artistsListPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseDetailsModel = repository.lookupRelease(
                    releaseId = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = releaseDetailsModel.getNameWithDisambiguation()
                subtitle = "Release by ${releaseDetailsModel.artistCredits.getDisplayNames()}"
                release = releaseDetailsModel
                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = screen.id,
                        title = title,
                        entity = screen.entity,
                    ),
                )
                recordedHistory = true
            }
            forceRefreshDetails = false
        }

        // Image fetching was split off from details model so that we can display data before images load
        LaunchedEffect(forceRefreshDetails, release) {
            release = release?.copy(
                imageMetadata = imageMetadataRepository.getAndSaveImageMetadata(
                    mbid = release?.id ?: return@LaunchedEffect,
                    entity = MusicBrainzEntity.RELEASE,
                    forceRefresh = forceRefreshDetails,
                ),
            )
            release?.let { release ->
                numberOfImages = imageMetadataRepository.getNumberOfImageMetadataById(release.id)
            }
        }

        LaunchedEffect(forceRefreshDetails, release) {
            wikimediaRepository.getWikipediaExtract(
                mbid = release?.id ?: return@LaunchedEffect,
                urls = release?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                release = release?.copy(
                    wikipediaExtract = wikipediaExtract,
                )
            }.onFailure {
                snackbarMessage = it.message
            }
        }

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
            topAppBarFilterState.show(
                selectedTab !in listOf(
                    ReleaseTab.STATS,
                ),
            )
            val browseMethod = BrowseMethod.ByEntity(
                entityId = screen.id,
                entity = screen.entity,
            )
            when (selectedTab) {
                ReleaseTab.DETAILS -> {
                    // Loaded above
                }

                ReleaseTab.TRACKS -> {
                    tracksEventSink(
                        TracksByEntityUiEvent.Get(
                            byEntityId = screen.id,
                        ),
                    )
                    tracksEventSink(TracksByEntityUiEvent.UpdateQuery(query))
                }

                ReleaseTab.ARTISTS -> {
                    artistsEventSink(
                        ArtistsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
                }

                ReleaseTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                ReleaseTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: ReleaseUiEvent) {
            when (event) {
                ReleaseUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ReleaseUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is ReleaseUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }

                ReleaseUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }

                is ReleaseUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                id = screen.id,
                            ),
                        ),
                    )
                }

                is ReleaseUiEvent.ToggleCollapseExpandReleaseEvents -> {
                    isReleaseEventsCollapsed = !isReleaseEventsCollapsed
                }
            }
        }

        return ReleaseUiState(
            title = title,
            subtitle = subtitle,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            release = release?.copy(
                labels = release?.labels
                    ?.filter { label ->
                        val searchText = query.lowercase()
                        listOf(
                            label.getNameWithDisambiguation(),
                            label.type,
                            label.labelCode.toString(),
                            label.catalogNumbers,
                        ).any { it?.lowercase()?.contains(searchText) == true }
                    }.orEmpty(),
                areas = release?.areas
                    ?.filter { area ->
                        val searchText = query.lowercase()
                        listOf(
                            area.getNameWithDisambiguation(),
                            area.date,
                        ).any { it?.lowercase()?.contains(searchText) == true }
                    }.orEmpty(),
                urls = release?.urls.filterUrlRelations(query = query),
            ),
            url = getMusicBrainzUrl(screen.entity, screen.id),
            releaseDetailsUiState = ReleaseDetailsUiState(
                handledException = handledException,
                numberOfImages = numberOfImages,
                lazyListState = detailsLazyListState,
                isReleaseEventsCollapsed = isReleaseEventsCollapsed,
            ),
            relationsUiState = relationsUiState,
            tracksByReleaseUiState = tracksByReleaseUiState,
            artistsListUiState = artistsByEntityUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ReleaseUiState(
    val title: String,
    val subtitle: String,
    val tabs: ImmutableList<ReleaseTab>,
    val selectedTab: ReleaseTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val release: ReleaseDetailsModel?,
    val url: String = "",
    val releaseDetailsUiState: ReleaseDetailsUiState,
    val relationsUiState: RelationsUiState,
    val tracksByReleaseUiState: TracksByReleaseUiState,
    val artistsListUiState: ArtistsListUiState,
    val loginUiState: LoginUiState,
    val eventSink: (ReleaseUiEvent) -> Unit,
) : CircuitUiState

internal data class ReleaseDetailsUiState(
    val handledException: HandledException? = null,
    val numberOfImages: Int? = null,
    val lazyListState: LazyListState = LazyListState(),
    val isReleaseEventsCollapsed: Boolean = false,
)

internal sealed interface ReleaseUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseUiEvent

    data object ForceRefresh : ReleaseUiEvent

    data class UpdateTab(val tab: ReleaseTab) : ReleaseUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseUiEvent

    data object ClickImage : ReleaseUiEvent

    data object ToggleCollapseExpandReleaseEvents : ReleaseUiEvent
}
