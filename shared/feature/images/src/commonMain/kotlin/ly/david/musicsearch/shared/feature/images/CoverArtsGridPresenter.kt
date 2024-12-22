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

internal class CoverArtsGridPresenter(
    private val screen: CoverArtsGridScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
    private val getMusicBrainzCoverArtUrl: GetMusicBrainzCoverArtUrl,
) : Presenter<CoverArtsGridUiState> {

    @Composable
    override fun present(): CoverArtsGridUiState {
        val imageUrls by rememberRetained {
            mutableStateOf(releaseImageRepository.getAllUrlsById(screen.id))
        }
        val title by rememberRetained {
            mutableStateOf(
                "Cover arts",
            )
        }

        fun eventSink(event: CoverArtsGridUiEvent) {
            when (event) {
                CoverArtsGridUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CoverArtsGridUiEvent.SelectImage -> {
                    navigator.goTo(
                        CoverArtsPagerScreen(
                            id = screen.id,
                            index = event.index,
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
    val eventSink: (CoverArtsGridUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsGridUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsGridUiEvent

    data class SelectImage(val index: Int) : CoverArtsGridUiEvent
}
