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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListUiEvent
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.screen.ArtistCollaborationScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import ly.david.musicsearch.ui.common.work.WorksListUiEvent
import ly.david.musicsearch.ui.common.work.WorksListUiState

internal class ArtistPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ArtistRepository,
    private val artistImageRepository: ArtistImageRepository,
    private val wikimediaRepository: WikimediaRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val eventsListPresenter: EventsListPresenter,
    private val recordingsListPresenter: RecordingsListPresenter,
    private val releasesListPresenter: ReleasesListPresenter,
    private val releaseGroupsListPresenter: ReleaseGroupsListPresenter,
    private val worksListPresenter: WorksListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) : Presenter<ArtistUiState>, RecordVisit {

    @Composable
    override fun present(): ArtistUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isLoading by rememberSaveable { mutableStateOf(true) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var artist: ArtistDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<ArtistTab> by rememberSaveable {
            mutableStateOf(ArtistTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val eventsByEntityUiState = eventsListPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsListPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesListPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsListPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink
        val worksByEntityUiState = worksListPresenter.present()
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
                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
            isLoading = false
            forceRefreshDetails = false
        }

        RecordVisit(
            mbid = screen.id,
            title = title,
            entity = screen.entity,
            searchHint = artist?.sortName,
        )

        LaunchedEffect(forceRefreshDetails, artist) {
            artist = artist?.copy(
                imageMetadata = artistImageRepository.getArtistImageMetadata(
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
            key1 = query,
            key2 = selectedTab,
        ) {
            topAppBarFilterState.show(
                selectedTab !in listOf(
                    ArtistTab.STATS,
                ),
            )
            val browseMethod = BrowseMethod.ByEntity(
                entityId = screen.id,
                entity = screen.entity,
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
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                ArtistTab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    recordingsEventSink(RecordingsListUiEvent.UpdateQuery(query))
                }

                ArtistTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
                }

                ArtistTab.RELEASE_GROUPS -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = true,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateQuery(query))
                }

                ArtistTab.EVENTS -> {
                    eventsEventSink(
                        EventsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    eventsEventSink(EventsListUiEvent.UpdateQuery(query))
                }

                ArtistTab.WORKS -> {
                    worksEventSink(
                        WorksListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    worksEventSink(WorksListUiEvent.UpdateQuery(query))
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

                        ArtistTab.RELEASES -> {
                            releasesByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.RECORDINGS -> {
                            recordingsByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.WORKS -> {
                            worksByEntityUiState.lazyPagingItems.refresh()
                        }

                        ArtistTab.RELATIONSHIPS -> {
                            relationsUiState.lazyPagingItems.refresh()
                        }

                        else -> {
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
            handledException = handledException,
            artist = artist?.copy(
                urls = artist?.urls.filterUrlRelations(query = query),
            ),
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            eventsListUiState = eventsByEntityUiState,
            recordingsListUiState = recordingsByEntityUiState,
            releaseGroupsListUiState = releaseGroupsByEntityUiState,
            releasesListUiState = releasesByEntityUiState,
            worksListUiState = worksByEntityUiState,
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
    val handledException: HandledException?,
    val artist: ArtistDetailsModel?,
    val url: String = "",
    val tabs: List<ArtistTab>,
    val selectedTab: ArtistTab,
    val topAppBarFilterState: TopAppBarFilterState,
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val eventsListUiState: EventsListUiState,
    val recordingsListUiState: RecordingsListUiState,
    val releasesListUiState: ReleasesListUiState,
    val releaseGroupsListUiState: ReleaseGroupsListUiState,
    val worksListUiState: WorksListUiState,
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
