package ly.david.musicsearch.shared.feature.details.artist

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
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.event.EventsByEntityPresenter
import ly.david.musicsearch.ui.common.event.EventsByEntityUiEvent
import ly.david.musicsearch.ui.common.event.EventsByEntityUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.work.WorksByEntityPresenter
import ly.david.musicsearch.ui.common.work.WorksByEntityUiEvent
import ly.david.musicsearch.ui.common.work.WorksByEntityUiState

internal class ArtistPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ArtistRepository,
    private val artistImageRepository: ArtistImageRepository,
    private val wikimediaRepository: WikimediaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val eventsByEntityPresenter: EventsByEntityPresenter,
    private val recordingsByEntityPresenter: RecordingsByEntityPresenter,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val releaseGroupsByEntityPresenter: ReleaseGroupsByEntityPresenter,
    private val worksByEntityPresenter: WorksByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) : Presenter<ArtistUiState> {

    @Composable
    override fun present(): ArtistUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isLoading by rememberSaveable { mutableStateOf(true) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var artist: ArtistDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<ArtistTab> by rememberSaveable {
            mutableStateOf(ArtistTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val eventsByEntityUiState = eventsByEntityPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsByEntityPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsByEntityPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink
        val worksByEntityUiState = worksByEntityPresenter.present()
        val worksEventSink = worksByEntityUiState.eventSink
        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                isLoading = true
                val artistDetailsModel = repository.lookupArtistDetails(
                    artistId = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = artistDetailsModel.getNameWithDisambiguation()
                artist = artistDetailsModel
                isError = false
            } catch (ex: HandledException) {
                logger.e(ex)
                isError = true
            }
            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = screen.id,
                        title = title,
                        entity = screen.entity,
                        searchHint = artist?.sortName.orEmpty(),
                    ),
                )
                recordedHistory = true
            }
            isLoading = false
            forceRefreshDetails = false
        }

        LaunchedEffect(forceRefreshDetails, artist) {
            artist = artist?.copy(
                imageUrls = artistImageRepository.getArtistImageUrl(
                    artistDetailsModel = artist ?: return@LaunchedEffect,
                    forceRefresh = forceRefreshDetails,
                ),
            )
        }

        LaunchedEffect(forceRefreshDetails, artist) {
            wikimediaRepository.getWikipediaExtract(
                mbid = artist?.id ?: return@LaunchedEffect,
                urls = artist?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                artist = artist?.copy(
                    wikipediaExtract = wikipediaExtract,
                )
            }.onFailure {
                snackbarMessage = it.message
            }
        }

        LaunchedEffect(
            key1 = topAppBarFilterState.filterText,
            key2 = selectedTab,
        ) {
            topAppBarFilterState.show(
                selectedTab !in listOf(
                    ArtistTab.STATS,
                ),
            )
            when (selectedTab) {
                ArtistTab.DETAILS -> {
                    // Loaded above
                }

                ArtistTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    recordingsEventSink(RecordingsByEntityUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.RELEASE_GROUPS -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                            isRemote = true,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsByEntityUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.EVENTS -> {
                    eventsEventSink(
                        EventsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    eventsEventSink(EventsByEntityUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.WORKS -> {
                    worksEventSink(
                        WorksByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    worksEventSink(WorksByEntityUiEvent.UpdateQuery(topAppBarFilterState.filterText))
                }

                ArtistTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: ArtistUiEvent) {
            when (event) {
                ArtistUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ArtistUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is ArtistUiEvent.ClickItem -> {
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

                ArtistUiEvent.ForceRefresh -> {
                    when (selectedTab) {
                        ArtistTab.DETAILS -> {
                            forceRefreshDetails = true
                        }

                        ArtistTab.RELEASE_GROUPS -> {
                            releaseGroupsByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.RELEASES -> {
                            releasesByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.RECORDINGS -> {
                            recordingsByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.WORKS -> {
                            worksByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.EVENTS -> {
                            eventsByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.RELATIONSHIPS -> {
                            relationsUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.STATS -> {
                            // No-op.
                        }
                    }
                }

                ArtistUiEvent.NavigateToCollaboratorsGraph -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            ArtistCollaborationScreen(
                                id = screen.id,
                                name = title,
                            ),
                        ),
                    )
                }
            }
        }

        return ArtistUiState(
            title = title,
            isLoading = isLoading,
            isError = isError,
            artist = artist,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            eventsByEntityUiState = eventsByEntityUiState,
            recordingsByEntityUiState = recordingsByEntityUiState,
            releaseGroupsByEntityUiState = releaseGroupsByEntityUiState,
            releasesByEntityUiState = releasesByEntityUiState,
            worksByEntityUiState = worksByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class ArtistUiState(
    val title: String,
    val isLoading: Boolean,
    val isError: Boolean,
    val artist: ArtistDetailsModel?,
    val url: String = "",
    val tabs: List<ArtistTab>,
    val selectedTab: ArtistTab,
    val topAppBarFilterState: TopAppBarFilterState,
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val eventsByEntityUiState: EventsByEntityUiState,
    val recordingsByEntityUiState: RecordingsByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    val worksByEntityUiState: WorksByEntityUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState,
    val eventSink: (ArtistUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ArtistUiEvent : CircuitUiEvent {
    data object NavigateUp : ArtistUiEvent
    data object ForceRefresh : ArtistUiEvent
    data class UpdateTab(val tab: ArtistTab) : ArtistUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ArtistUiEvent

    data object NavigateToCollaboratorsGraph : ArtistUiEvent
}
