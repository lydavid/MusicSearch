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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal val eventTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELATIONSHIPS,
    Tab.STATS,
)
internal class EventPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: EventRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<EventUiState>, RecordVisit {

    @Composable
    override fun present(): EventUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        val tabs: ImmutableList<Tab> = eventTabs
        var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var event: EventDetailsModel? by rememberRetained { mutableStateOf(null) }
        var numberOfImages: Int? by rememberSaveable { mutableStateOf(null) }
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
            searchHint = "",
        )

        LaunchedEffect(forceRefreshDetails, event) {
            val imageMetadataWithCount = musicBrainzImageMetadataRepository.getAndSaveImageMetadata(
                mbid = event?.id ?: return@LaunchedEffect,
                entity = MusicBrainzEntity.EVENT,
                forceRefresh = forceRefreshDetails,
            )
            event = event?.copy(
                imageMetadata = imageMetadataWithCount.imageMetadata,
            )
            numberOfImages = imageMetadataWithCount.count
            forceRefreshDetails = false
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
                    Tab.STATS,
                ),
            )
            when (selectedTab) {
                Tab.DETAILS -> {
                    // Loaded above
                }

                Tab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                else -> {
                    // no-op
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

                EventUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                id = screen.id,
                                entity = screen.entity,
                            ),
                        ),
                    )
                }

                EventUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
                }
            }
        }

        return EventUiState(
            title = title,
            handledException = handledException,
            event = event?.copy(
                urls = event?.urls.filterUrlRelations(query = query),
            ),
            numberOfImages = numberOfImages,
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
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab = Tab.DETAILS,
    val handledException: HandledException? = null,
    val event: EventDetailsModel? = null,
    val numberOfImages: Int? = null,
    val url: String = "",
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (EventUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface EventUiEvent : CircuitUiEvent {
    data object NavigateUp : EventUiEvent
    data object ForceRefreshDetails : EventUiEvent
    data class UpdateTab(val tab: Tab) : EventUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : EventUiEvent

    data object ClickImage : EventUiEvent
}
