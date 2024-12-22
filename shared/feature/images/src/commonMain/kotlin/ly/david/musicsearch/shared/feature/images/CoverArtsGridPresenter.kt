package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsGridScreen
import ly.david.musicsearch.ui.common.screen.CoverArtsPagerScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CoverArtsGridPresenter(
    private val screen: CoverArtsGridScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
    private val getMusicBrainzCoverArtUrl: GetMusicBrainzCoverArtUrl,
) : Presenter<CoverArtsGridUiState> {

    @Composable
    override fun present(): CoverArtsGridUiState {
        val title by rememberRetained {
            mutableStateOf(
                "Cover arts",
            )
        }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val imageUrls by rememberRetained(topAppBarFilterState.filterText) {
            mutableStateOf(
                releaseImageRepository.getAllUrlsById(
                    mbid = screen.id,
                    query = topAppBarFilterState.filterText,
                ),
            )
        }

        fun eventSink(event: CoverArtsGridUiEvent) {
            when (event) {
                CoverArtsGridUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CoverArtsGridUiEvent.UpdateQuery -> {
                    topAppBarFilterState.updateFilterText(event.query)
                }

                is CoverArtsGridUiEvent.SelectImage -> {
                    navigator.goTo(
                        CoverArtsPagerScreen(
                            id = screen.id,
                            index = event.index,
                            query = topAppBarFilterState.filterText,
                        ),
                    )
                }
            }
        }

        return CoverArtsGridUiState(
            id = screen.id,
            title = title,
            url = getMusicBrainzCoverArtUrl(screen.id),
            imageUrls = imageUrls,
            topAppBarFilterState = topAppBarFilterState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsGridUiState(
    val id: String,
    val title: String = "",
    val url: String = "",
    val imageUrls: List<ImageUrls>,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val eventSink: (CoverArtsGridUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsGridUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsGridUiEvent

    data class UpdateQuery(val query: String) : CoverArtsGridUiEvent

    data class SelectImage(val index: Int) : CoverArtsGridUiEvent
}
