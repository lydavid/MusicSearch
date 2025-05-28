package ly.david.musicsearch.shared.feature.details.series

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
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.shared.feature.details.utils.filterUrlRelations
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class SeriesPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: SeriesRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val wikimediaRepository: WikimediaRepository,
) : Presenter<SeriesUiState>, RecordVisit {

    @Composable
    override fun present(): SeriesUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var series: SeriesDetailsModel? by rememberRetained { mutableStateOf(null) }
        val tabs: List<SeriesTab> by rememberSaveable {
            mutableStateOf(SeriesTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(SeriesTab.DETAILS) }
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
                val seriesDetailsModel = repository.lookupSeries(
                    seriesId = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = seriesDetailsModel.getNameWithDisambiguation()
                series = seriesDetailsModel
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

        LaunchedEffect(forceRefreshDetails, series) {
            wikimediaRepository.getWikipediaExtract(
                mbid = series?.id ?: return@LaunchedEffect,
                urls = series?.urls ?: return@LaunchedEffect,
                forceRefresh = forceRefreshDetails,
            ).onSuccess { wikipediaExtract ->
                series = series?.copy(
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
                    SeriesTab.STATS,
                ),
            )
            when (selectedTab) {
                SeriesTab.DETAILS -> {
                    // Loaded above
                }

                SeriesTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                SeriesTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: SeriesUiEvent) {
            when (event) {
                SeriesUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is SeriesUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is SeriesUiEvent.ClickItem -> {
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

                SeriesUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return SeriesUiState(
            title = title,
            handledException = handledException,
            series = series?.copy(
                urls = series?.urls.filterUrlRelations(query = query),
            ),
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
internal data class SeriesUiState(
    val title: String,
    val handledException: HandledException?,
    val series: SeriesDetailsModel?,
    val url: String = "",
    val tabs: List<SeriesTab>,
    val selectedTab: SeriesTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val snackbarMessage: String? = null,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (SeriesUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SeriesUiEvent : CircuitUiEvent {
    data object NavigateUp : SeriesUiEvent
    data object ForceRefresh : SeriesUiEvent
    data class UpdateTab(val tab: SeriesTab) : SeriesUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : SeriesUiEvent
}
