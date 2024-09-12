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
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleaseDetailsModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiState
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
    private val releaseImageRepository: ReleaseImageRepository,
    private val tracksByReleasePresenter: TracksByReleasePresenter,
    private val artistsByEntityPresenter: ArtistsByEntityPresenter,
    private val logger: Logger,
) : Presenter<ReleaseUiState> {

    @Composable
    override fun present(): ReleaseUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var release: ReleaseDetailsModel? by remember { mutableStateOf(null) }
        var imageUrl by rememberSaveable { mutableStateOf("") }
        val tabs: ImmutableList<ReleaseTab> = ReleaseTab.entries.toPersistentList()
        var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val tracksByReleaseUiState = tracksByReleasePresenter.present()
        val tracksEventSink = tracksByReleaseUiState.eventSink

        val artistsByEntityUiState = artistsByEntityPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseDetailsModel = repository.lookupRelease(
                    releaseId = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = releaseDetailsModel.getNameWithDisambiguation()
                subtitle = "Release by ${releaseDetailsModel.artistCredits.getDisplayNames()}"
                release = releaseDetailsModel
                imageUrl = fetchReleaseImage(releaseDetailsModel)
                isError = false
            } catch (ex: RecoverableNetworkException) {
                logger.e(ex)
                isError = true
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
        }

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
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
                        ArtistsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    artistsEventSink(ArtistsByEntityUiEvent.UpdateQuery(query))
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
            }
        }

        return ReleaseUiState(
            title = title,
            subtitle = subtitle,
            isError = isError,
            release = release,
            imageUrl = imageUrl,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            relationsUiState = relationsUiState,
            tracksByReleaseUiState = tracksByReleaseUiState,
            artistsByEntityUiState = artistsByEntityUiState,
            eventSink = ::eventSink,
        )
    }

    private suspend fun fetchReleaseImage(
        releaseDetailsModel: ReleaseDetailsModel,
    ): String {
        val imageUrl = releaseDetailsModel.imageUrl
        return imageUrl ?: releaseImageRepository.getReleaseCoverArtUrlsFromNetworkAndSave(
            releaseId = releaseDetailsModel.id,
            thumbnail = false,
        )
    }
}

@Stable
internal data class ReleaseUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val release: ReleaseDetailsModel?,
    val imageUrl: String,
    val tabs: ImmutableList<ReleaseTab>,
    val selectedTab: ReleaseTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val relationsUiState: RelationsUiState,
    val tracksByReleaseUiState: TracksByReleaseUiState,
    val artistsByEntityUiState: ArtistsByEntityUiState,
    val eventSink: (ReleaseUiEvent) -> Unit,
) : CircuitUiState

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
}
