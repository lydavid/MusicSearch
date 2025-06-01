package ly.david.musicsearch.shared.feature.details.place

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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.relatableEntities
import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListUiEvent
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal val placeTabs = persistentListOf(
    Tab.DETAILS,

    // TODO: Should exclude event-rels because they appear to be the same as the results from browse events by place
    Tab.RELATIONSHIPS,
    Tab.EVENTS,
    Tab.STATS,
)

internal class PlacePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: PlaceRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val eventsListPresenter: EventsListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<PlaceUiState>, RecordVisit {

    @Composable
    override fun present(): PlaceUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        val tabs: ImmutableList<Tab> = placeTabs
        var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var place: PlaceDetailsModel? by rememberRetained { mutableStateOf(null) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val eventsByEntityUiState = eventsListPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val placeDetailsModel = repository.lookupPlace(
                    screen.id,
                    forceRefreshDetails,
                )
                title = placeDetailsModel.getNameWithDisambiguation()
                place = placeDetailsModel
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

        LaunchedEffect(forceRefreshDetails, place) {
            wikimediaRepository.getWikipediaExtract(
                mbid = place?.id ?: return@LaunchedEffect,
                urls = place?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                place = place?.copy(
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
            val browseMethod = BrowseMethod.ByEntity(
                entityId = screen.id,
                entity = screen.entity,
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
                            relatedEntities = relatableEntities subtract setOf(
                                MusicBrainzEntity.EVENT,
                                MusicBrainzEntity.URL,
                            ),
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                Tab.EVENTS -> {
                    eventsEventSink(
                        EventsListUiEvent.Get(
                            browseMethod = browseMethod,
                        ),
                    )
                    eventsEventSink(EventsListUiEvent.UpdateQuery(query))
                }

                else -> {
                    // no-op
                }
            }
        }

        fun eventSink(event: PlaceUiEvent) {
            when (event) {
                PlaceUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is PlaceUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is PlaceUiEvent.ClickItem -> {
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

                PlaceUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return PlaceUiState(
            title = title,
            handledException = handledException,
            place = place?.copy(
                urls = place?.urls.filterUrlRelations(query = query),
            ),
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            eventsListUiState = eventsByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class PlaceUiState(
    val title: String,
    val tabs: ImmutableList<Tab>,
    val selectedTab: Tab,
    val handledException: HandledException?,
    val place: PlaceDetailsModel?,
    val url: String = "",
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val eventsListUiState: EventsListUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (PlaceUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface PlaceUiEvent : CircuitUiEvent {
    data object NavigateUp : PlaceUiEvent
    data object ForceRefresh : PlaceUiEvent
    data class UpdateTab(val tab: Tab) : PlaceUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : PlaceUiEvent
}
