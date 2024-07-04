package ly.david.musicsearch.shared.feature.details.release

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
import ly.david.musicsearch.core.models.release.ReleaseScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.track.TracksByEntityUiEvent
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState

internal class ReleasePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ReleaseRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val releaseImageRepository: ReleaseImageRepository,
    private val tracksByReleasePresenter: TracksByReleasePresenter,
    private val artistsByEntityPresenter: ArtistsByEntityPresenter,
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

        val tracksByReleaseUiState = tracksByReleasePresenter.present()
        val tracksEventSink = tracksByReleaseUiState.eventSink

        val artistsByEntityUiState = artistsByEntityPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink

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

                ReleaseTab.TRACKS -> {
                    tracksEventSink(
                        TracksByEntityUiEvent.Get(
                            byEntityId = screen.id,
                        ),
                    )
                    tracksEventSink(TracksByEntityUiEvent.UpdateQuery(query))
                }

                ReleaseTab.ARTISTS -> {
                    artistsEventSink(
                        ArtistsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    artistsEventSink(ArtistsByEntityUiEvent.UpdateQuery(query))
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

                is ReleaseUiEvent.ClickImage -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            CoverArtsScreen(
                                id = screen.id,
                            ),
                        ),
                    )
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
            tracksByReleaseUiState = tracksByReleaseUiState,
            artistsByEntityUiState = artistsByEntityUiState,
            eventSink = ::eventSink,
        )
    }

    private suspend fun fetchReleaseImage(
        releaseScaffoldModel: ReleaseScaffoldModel,
    ): String {
        val imageUrl = releaseScaffoldModel.imageUrl
        return imageUrl ?: releaseImageRepository.getReleaseCoverArtUrlsFromNetworkAndSave(
            releaseId = releaseScaffoldModel.id,
            thumbnail = false,
        )
    }
}

@Stable
internal data class ReleaseUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val release: ReleaseScaffoldModel?,
    val imageUrl: String,
    val tabs: List<ReleaseTab>,
    val selectedTab: ReleaseTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val tracksByReleaseUiState: TracksByReleaseUiState,
    val artistsByEntityUiState: ArtistsByEntityUiState,
    val eventSink: (ReleaseUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ReleaseUiEvent : CircuitUiEvent {
    data object NavigateUp : ReleaseUiEvent
    data object ForceRefresh : ReleaseUiEvent
    data class UpdateTab(val tab: ReleaseTab) : ReleaseUiEvent
    data class UpdateQuery(val query: String) : ReleaseUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : ReleaseUiEvent
    data object ClickImage: ReleaseUiEvent
}
