package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.release.ReleaseImageRepository
import ly.david.musicsearch.domain.release.ReleaseRepository
import ly.david.musicsearch.domain.release.usecase.GetTracksByRelease
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.screen.DetailsScreen

internal class ReleasePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ReleaseRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val releaseImageRepository: ReleaseImageRepository,
    private val getTracksByRelease: GetTracksByRelease,
    private val logger: Logger,
) : Presenter<ReleaseUiState> {

    @Composable
    override fun present(): ReleaseUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var release: ReleaseScaffoldModel? by remember { mutableStateOf(null) }
        var imageUrl by rememberSaveable { mutableStateOf("") }
        val tabs: List<ReleaseTab> by rememberSaveable {
            mutableStateOf(ReleaseTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ReleaseTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        // TODO: if we want these to maintain their scroll position, need to move to TracksByReleasePresenter
        var tracksListItems: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseScaffoldModel = repository.lookupRelease(screen.id)
                if (title.isEmpty()) {
                    title = releaseScaffoldModel.getNameWithDisambiguation()
                }
                subtitle = "Release by ${releaseScaffoldModel.artistCredits.getDisplayNames()}"
                release = releaseScaffoldModel
                imageUrl = fetchReleaseImage(releaseScaffoldModel)

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

                ReleaseTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                ReleaseTab.TRACKS -> {
                    tracksListItems = getTracksByRelease(
                        releaseId = screen.id,
                        query = query,
                    )
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

                is ReleaseUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            relationsUiState = relationsUiState,
            tracksLazyPagingItems = tracksListItems.collectAsLazyPagingItems(),
            eventSink = ::eventSink,
        )
    }

    private suspend fun fetchReleaseImage(
        releaseScaffoldModel: ReleaseScaffoldModel,
    ): String {
        val imageUrl = releaseScaffoldModel.imageUrl
        return imageUrl ?: releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
            releaseId = releaseScaffoldModel.id,
            thumbnail = false,
        )
    }
}
