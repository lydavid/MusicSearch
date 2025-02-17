package ly.david.musicsearch.shared.feature.details.event

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
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class EventPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: EventRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val imageMetadataRepository: ImageMetadataRepository,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<EventUiState> {

    @Composable
    override fun present(): EventUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var event: EventDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<EventTab> by rememberSaveable {
            mutableStateOf(EventTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(EventTab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val eventListItemModel = repository.lookupEvent(
                    screen.id,
                    forceRefreshDetails,
                )
                title = eventListItemModel.getNameWithDisambiguation()
                event = eventListItemModel
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

        LaunchedEffect(forceRefreshDetails, event) {
            event = event?.copy(
                imageMetadata = imageMetadataRepository.getImageMetadata(
                    mbid = event?.id ?: return@LaunchedEffect,
                    entity = MusicBrainzEntity.EVENT,
                    forceRefresh = forceRefreshDetails,
                ),
            )
        }

        LaunchedEffect(forceRefreshDetails, event) {
            wikimediaRepository.getWikipediaExtract(
                mbid = event?.id ?: return@LaunchedEffect,
                urls = event?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                event = event?.copy(
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
                    EventTab.STATS,
                ),
            )
            when (selectedTab) {
                EventTab.DETAILS -> {
                    // Loaded above
                }

                EventTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                EventTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: EventUiEvent) {
            when (event) {
                EventUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is EventUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is EventUiEvent.ClickItem -> {
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

                EventUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return EventUiState(
            title = title,
            isError = isError,
            event = event,
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class EventUiState(
    val title: String,
    val isError: Boolean = false,
    val event: EventDetailsModel? = null,
    val url: String = "",
    val tabs: List<EventTab> = listOf(),
    val selectedTab: EventTab = EventTab.DETAILS,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (EventUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface EventUiEvent : CircuitUiEvent {
    data object NavigateUp : EventUiEvent
    data object ForceRefresh : EventUiEvent
    data class UpdateTab(val tab: EventTab) : EventUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : EventUiEvent
}
