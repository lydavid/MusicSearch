package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsPagerScreen

internal class CoverArtsPagerPresenter(
    private val screen: CoverArtsPagerScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<CoverArtsPagerUiState> {

    @Composable
    override fun present(): CoverArtsPagerUiState {
        val imageUrls by rememberRetained {
            mutableStateOf(releaseImageRepository.getAllUrls(screen.id))
        }
        var selectedImageIndex by rememberRetained {
            mutableStateOf(screen.index)
        }
        val title by rememberRetained(selectedImageIndex) {
            val imageUrl = imageUrls[selectedImageIndex]
            val typeAndComment = imageUrl.types.joinToString().appendOptionalText(imageUrl.comment)
            mutableStateOf(
                // Previously cached images will not have a type or comment stored,
                // so make sure we don't show a loading indicator
                typeAndComment.ifEmpty { " " },
            )
        }
        val subtitle by rememberRetained(selectedImageIndex) {
            mutableStateOf("${selectedImageIndex + 1} / ${imageUrls.size}")
        }

        fun eventSink(event: CoverArtsPagerUiEvent) {
            when (event) {
                CoverArtsPagerUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CoverArtsPagerUiEvent.ChangeImage -> {
                    selectedImageIndex = event.index
                }
            }
        }

        return CoverArtsPagerUiState(
            id = screen.id,
            title = title,
            subtitle = subtitle,
            imageUrls = imageUrls,
            selectedImageIndex = selectedImageIndex,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsPagerUiState(
    val id: String,
    val title: String = "",
    val subtitle: String = "",
    val imageUrls: List<ImageUrls>,
    val selectedImageIndex: Int = 0,
    val eventSink: (CoverArtsPagerUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsPagerUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsPagerUiEvent

    data class ChangeImage(val index: Int) : CoverArtsPagerUiEvent
}
