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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import ly.david.musicsearch.shared.domain.work.WorkRepository
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class WorkPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: WorkRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val artistsListPresenter: ArtistsListPresenter,
    private val recordingsListPresenter: RecordingsListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<WorkUiState>, RecordVisit {

    @Composable
    override fun present(): WorkUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var work: WorkDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<WorkTab> by rememberSaveable {
            mutableStateOf(WorkTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(WorkTab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val artistsByEntityUiState = artistsListPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsListPresenter.present()
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
                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
        }

        RecordVisit(
            mbid = screen.id,
            title = title,
            entity = screen.entity,
        )

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
            topAppBarFilterState.show(
                selectedTab !in listOf(
                    WorkTab.STATS,
                ),
            )
            val browseMethod = BrowseMethod.ByEntity(
                entityId = screen.id,
                entity = screen.entity,
            )
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
                        ArtistsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
                }

                WorkTab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    recordingsEventSink(RecordingsListUiEvent.UpdateQuery(query))
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
            handledException = handledException,
            work = work?.copy(
                urls = work?.urls.filterUrlRelations(query = query),
            ),
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            artistsListUiState = artistsByEntityUiState,
            recordingsListUiState = recordingsByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class WorkUiState(
    val title: String,
    val handledException: HandledException?,
    val work: WorkDetailsModel?,
    val url: String = "",
    val tabs: List<WorkTab>,
    val selectedTab: WorkTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val artistsListUiState: ArtistsListUiState,
    val recordingsListUiState: RecordingsListUiState,
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
