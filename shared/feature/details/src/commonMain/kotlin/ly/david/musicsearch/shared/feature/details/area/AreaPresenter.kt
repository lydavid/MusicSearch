package ly.david.musicsearch.shared.feature.details.area

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
import ly.david.musicsearch.shared.domain.area.AreaDetailsModel
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListUiEvent
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListUiEvent
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListUiEvent
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal val areaTabs = persistentListOf(
    Tab.DETAILS,
    Tab.RELATIONSHIPS,
    Tab.ARTISTS,
    Tab.EVENTS,
    Tab.LABELS,
    Tab.RELEASES,
    Tab.PLACES,
    Tab.STATS,
)

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val artistsListPresenter: ArtistsListPresenter,
    private val eventsListPresenter: EventsListPresenter,
    private val labelsListPresenter: LabelsListPresenter,
    private val releasesListPresenter: ReleasesListPresenter,
    private val placesListPresenter: PlacesListPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<AreaUiState>, RecordVisit {

    @Composable
    override fun present(): AreaUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var area: AreaDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: ImmutableList<Tab> by rememberRetained {
            mutableStateOf(areaTabs)
        }
        var selectedTab by rememberSaveable { mutableStateOf(Tab.DETAILS) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var forceRefreshDetails by remember { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()
        var snackbarMessage: String? by rememberSaveable { mutableStateOf(null) }

        val artistsByEntityUiState = artistsListPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val eventsByEntityUiState = eventsListPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val labelsByEntityUiState = labelsListPresenter.present()
        val labelsEventSink = labelsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesListPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val placesByEntityUiState = placesListPresenter.present()
        val placesEventSink = placesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val areaDetailsModel = repository.lookupArea(
                    screen.id,
                    forceRefreshDetails,
                )
                title = areaDetailsModel.getNameWithDisambiguation()
                area = areaDetailsModel
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
            searchHint = area?.sortName,
        )

        LaunchedEffect(forceRefreshDetails, area) {
            wikimediaRepository.getWikipediaExtract(
                mbid = area?.id ?: return@LaunchedEffect,
                urls = area?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                area = area?.copy(
                    wikipediaExtract = wikipediaExtract,
                )
            }.onFailure {
                snackbarMessage = it.message
            }
        }

        LoadListItems(
            screen = screen,
            query = query,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            relationsEventSink = relationsEventSink,
            artistsEventSink = artistsEventSink,
            eventsEventSink = eventsEventSink,
            labelsEventSink = labelsEventSink,
            releasesEventSink = releasesEventSink,
            placesEventSink = placesEventSink,
        )

        fun eventSink(event: AreaUiEvent) {
            when (event) {
                AreaUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is AreaUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is AreaUiEvent.ClickItem -> {
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

                AreaUiEvent.ForceRefreshDetails -> {
                    forceRefreshDetails = true
                }
            }
        }

        return AreaUiState(
            title = title,
            handledException = handledException,
            area = area?.copy(
                urls = area?.urls.filterUrlRelations(query = query),
            ),
            url = getMusicBrainzUrl(screen.entity, screen.id),
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            snackbarMessage = snackbarMessage,
            artistsListUiState = artistsByEntityUiState,
            eventsListUiState = eventsByEntityUiState,
            labelsListUiState = labelsByEntityUiState,
            placesListUiState = placesByEntityUiState,
            releasesListUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Composable
private fun LoadListItems(
    screen: DetailsScreen,
    query: String,
    selectedTab: Tab,
    topAppBarFilterState: TopAppBarFilterState,
    relationsEventSink: (RelationsUiEvent) -> Unit,
    artistsEventSink: (ArtistsListUiEvent) -> Unit,
    eventsEventSink: (EventsListUiEvent) -> Unit,
    labelsEventSink: (LabelsListUiEvent) -> Unit,
    releasesEventSink: (ReleasesListUiEvent) -> Unit,
    placesEventSink: (PlacesListUiEvent) -> Unit,
) {
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
                    ),
                )
                relationsEventSink(RelationsUiEvent.UpdateQuery(query))
            }

            Tab.ARTISTS -> {
                artistsEventSink(
                    ArtistsListUiEvent.Get(
                        browseMethod = browseMethod,
                    ),
                )
                artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
            }

            Tab.EVENTS -> {
                eventsEventSink(
                    EventsListUiEvent.Get(
                        browseMethod = browseMethod,
                    ),
                )
                eventsEventSink(EventsListUiEvent.UpdateQuery(query))
            }

            Tab.LABELS -> {
                labelsEventSink(
                    LabelsListUiEvent.Get(
                        browseMethod = browseMethod,
                    ),
                )
                labelsEventSink(LabelsListUiEvent.UpdateQuery(query))
            }

            Tab.RELEASES -> {
                releasesEventSink(
                    ReleasesListUiEvent.Get(
                        browseMethod = browseMethod,
                    ),
                )
                releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
            }

            Tab.PLACES -> {
                placesEventSink(
                    PlacesListUiEvent.Get(
                        browseMethod = browseMethod,
                    ),
                )
                placesEventSink(PlacesListUiEvent.UpdateQuery(query))
            }

            else -> {
                // no-op
            }
        }
    }
}

@Stable
internal data class AreaUiState(
    val title: String,
    val tabs: ImmutableList<Tab>,
    val handledException: HandledException? = null,
    val area: AreaDetailsModel? = null,
    val url: String = "",
    val selectedTab: Tab = Tab.DETAILS,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val artistsListUiState: ArtistsListUiState,
    val eventsListUiState: EventsListUiState,
    val labelsListUiState: LabelsListUiState,
    val placesListUiState: PlacesListUiState,
    val releasesListUiState: ReleasesListUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (AreaUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface AreaUiEvent : CircuitUiEvent {
    data object NavigateUp : AreaUiEvent
    data object ForceRefreshDetails : AreaUiEvent
    data class UpdateTab(val tab: Tab) : AreaUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : AreaUiEvent
}
