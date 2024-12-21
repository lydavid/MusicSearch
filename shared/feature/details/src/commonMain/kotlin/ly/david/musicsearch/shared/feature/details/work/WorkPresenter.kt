package ly.david.musicsearch.shared.feature.details.work

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
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import ly.david.musicsearch.shared.domain.work.WorkRepository
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class WorkPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: WorkRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val artistsByEntityPresenter: ArtistsByEntityPresenter,
    private val recordingsByEntityPresenter: RecordingsByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<WorkUiState> {

    @Composable
    override fun present(): WorkUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var work: WorkDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<WorkTab> by rememberSaveable {
            mutableStateOf(WorkTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(WorkTab.DETAILS) }
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val artistsByEntityUiState = artistsByEntityPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsByEntityPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val workDetailsModel = repository.lookupWork(
                    screen.id,
                    forceRefreshDetails,
                )
                title = workDetailsModel.getNameWithDisambiguation()
                work = workDetailsModel
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
                    ),
                )
                recordedHistory = true
            }
        }

        LaunchedEffect(forceRefreshDetails, work) {
            wikimediaRepository.getWikipediaExtract(
                mbid = work?.id ?: return@LaunchedEffect,
                urls = work?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                work = work?.copy(
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
            when (selectedTab) {
                WorkTab.DETAILS -> {
                    // Loaded above
                }

                WorkTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                WorkTab.ARTISTS -> {
                    artistsEventSink(
                        ArtistsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    artistsEventSink(ArtistsByEntityUiEvent.UpdateQuery(query))
                }

                WorkTab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    recordingsEventSink(RecordingsByEntityUiEvent.UpdateQuery(query))
                }

                WorkTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: WorkUiEvent) {
            when (event) {
                WorkUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is WorkUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is WorkUiEvent.ClickItem -> {
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

                WorkUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return WorkUiState(
            title = title,
            isError = isError,
            work = work,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            artistsByEntityUiState = artistsByEntityUiState,
            recordingsByEntityUiState = recordingsByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class WorkUiState(
    val title: String,
    val isError: Boolean,
    val work: WorkDetailsModel?,
    val url: String = "",
    val tabs: List<WorkTab>,
    val selectedTab: WorkTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val artistsByEntityUiState: ArtistsByEntityUiState,
    val recordingsByEntityUiState: RecordingsByEntityUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (WorkUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface WorkUiEvent : CircuitUiEvent {
    data object NavigateUp : WorkUiEvent
    data object ForceRefresh : WorkUiEvent
    data class UpdateTab(val tab: WorkTab) : WorkUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : WorkUiEvent
}
