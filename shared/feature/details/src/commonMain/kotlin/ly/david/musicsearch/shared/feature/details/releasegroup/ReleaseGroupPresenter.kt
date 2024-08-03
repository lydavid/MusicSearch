package ly.david.musicsearch.shared.feature.details.releasegroup

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
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class ReleaseGroupPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ReleaseGroupRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
    private val logger: Logger,
) : Presenter<ReleaseGroupUiState> {

    @Composable
    override fun present(): ReleaseGroupUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var releaseGroup: ReleaseGroupDetailsModel? by remember { mutableStateOf(null) }
        var imageUrl by rememberSaveable { mutableStateOf("") }
        val tabs: List<ReleaseGroupTab> by rememberSaveable {
            mutableStateOf(ReleaseGroupTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseGroupDetailsModel = repository.lookupReleaseGroup(screen.id)
                if (title.isEmpty()) {
                    title = releaseGroupDetailsModel.getNameWithDisambiguation()
                }
                subtitle = "Release Group by ${releaseGroupDetailsModel.artistCredits.getDisplayNames()}"
                releaseGroup = releaseGroupDetailsModel
                imageUrl = fetchReleaseGroupImage(releaseGroupDetailsModel)

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
                ReleaseGroupTab.DETAILS -> {
                    // Loaded above
                }

                ReleaseGroupTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                ReleaseGroupTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                ReleaseGroupTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: ReleaseGroupUiEvent) {
            when (event) {
                ReleaseGroupUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ReleaseGroupUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is ReleaseGroupUiEvent.ClickItem -> {
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

                ReleaseGroupUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return ReleaseGroupUiState(
            title = title,
            subtitle = subtitle,
            isError = isError,
            releaseGroup = releaseGroup,
            imageUrl = imageUrl,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }

    private suspend fun fetchReleaseGroupImage(
        releaseGroupDetailsModel: ReleaseGroupDetailsModel,
    ): String {
        val imageUrl = releaseGroupDetailsModel.imageUrl
        return imageUrl ?: releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
            releaseGroupId = releaseGroupDetailsModel.id,
            thumbnail = false,
        )
    }
}

@Stable
internal data class ReleaseGroupUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val releaseGroup: ReleaseGroupDetailsModel?,
    val imageUrl: String,
    val tabs: List<ReleaseGroupTab>,
    val selectedTab: ReleaseGroupTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (ReleaseGroupUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ReleaseGroupUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseGroupUiEvent
    data object ForceRefresh : ReleaseGroupUiEvent
    data class UpdateTab(val tab: ReleaseGroupTab) : ReleaseGroupUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseGroupUiEvent
}
