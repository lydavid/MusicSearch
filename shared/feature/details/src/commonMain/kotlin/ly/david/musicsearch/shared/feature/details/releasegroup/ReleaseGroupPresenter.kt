package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupImageRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.screen.DetailsScreen

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
        var query by rememberSaveable { mutableStateOf("") }
        var releaseGroup: ReleaseGroupScaffoldModel? by remember { mutableStateOf(null) }
        var imageUrl by rememberSaveable { mutableStateOf("") }
        val tabs: List<ReleaseGroupTab> by rememberSaveable {
            mutableStateOf(ReleaseGroupTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ReleaseGroupTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val releaseGroupScaffoldModel = repository.lookupReleaseGroup(screen.id)
                if (title.isEmpty()) {
                    title = releaseGroupScaffoldModel.getNameWithDisambiguation()
                }
                subtitle = "Release Group by ${releaseGroupScaffoldModel.artistCredits.getDisplayNames()}"
                releaseGroup = releaseGroupScaffoldModel
                imageUrl = fetchReleaseGroupImage(releaseGroupScaffoldModel)

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

                is ReleaseGroupUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }

    private suspend fun fetchReleaseGroupImage(
        releaseGroupScaffoldModel: ReleaseGroupScaffoldModel,
    ): String {
        val imageUrl = releaseGroupScaffoldModel.imageUrl
        return imageUrl ?: releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
            releaseGroupId = releaseGroupScaffoldModel.id,
            thumbnail = false,
        )
    }
}
