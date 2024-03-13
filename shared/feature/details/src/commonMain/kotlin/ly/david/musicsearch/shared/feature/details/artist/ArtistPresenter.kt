package ly.david.musicsearch.shared.feature.details.artist

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
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.data.spotify.ArtistImageRepository
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.ui.common.screen.DetailsScreen

internal class ArtistPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: ArtistRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val releaseGroupsByEntityPresenter: ReleaseGroupsByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val artistImageRepository: ArtistImageRepository,
    private val logger: Logger,
) : Presenter<ArtistUiState> {

    private suspend fun fetchArtistImage(
        artist: ArtistScaffoldModel,
    ): String {
        val imageUrl = artist.imageUrl
        return if (imageUrl == null) {
            val spotifyUrl =
                artist.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name ?: return ""
            artistImageRepository.getArtistImageFromNetwork(
                artistMbid = artist.id,
                spotifyUrl = spotifyUrl,
            )
        } else {
            imageUrl
        }
    }

    @Composable
    override fun present(): ArtistUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var artist: ArtistScaffoldModel? by remember { mutableStateOf(null) }
        var imageUrl by rememberSaveable { mutableStateOf("") }
        var tabs: List<ArtistTab> by rememberSaveable {
            mutableStateOf(ArtistTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(ArtistTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsByEntityPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val artistScaffoldModel = repository.lookupArtist(screen.id)
                if (title.isEmpty()) {
                    title = artistScaffoldModel.getNameWithDisambiguation()
                }
                artist = artistScaffoldModel
                imageUrl = fetchArtistImage(artistScaffoldModel)
                isError = false
            } catch (ex: Exception) {
                logger.e(ex)
                isError = true
            }
            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = screen.id,
                        title = title,
                        entity = screen.entity,
                        searchHint = artist?.sortName.orEmpty(),
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
                ArtistTab.DETAILS -> {
                    // Loaded above
                }

                ArtistTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                ArtistTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                ArtistTab.RELEASE_GROUPS -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                            isRemote = true,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsByEntityUiEvent.UpdateQuery(query))
                }

                ArtistTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: ArtistUiEvent) {
            when (event) {
                ArtistUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is ArtistUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is ArtistUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is ArtistUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                event.entity,
                                event.id,
                                event.title,
                            ),
                        ),
                    )
                }

                ArtistUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return ArtistUiState(
            title = title,
            isError = isError,
            artist = artist,
            imageUrl = imageUrl,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            releaseGroupsByEntityUiState = releaseGroupsByEntityUiState,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}
