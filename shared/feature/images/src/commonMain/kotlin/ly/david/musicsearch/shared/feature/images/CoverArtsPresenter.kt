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
import ly.david.musicsearch.shared.domain.appendOptionalText
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen

internal const val NO_IMAGE_SELECTED_INDEX = -1

internal class CoverArtsPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<CoverArtsUiState> {

    @Composable
    override fun present(): CoverArtsUiState {
        val imageUrls by rememberRetained {
            mutableStateOf(releaseImageRepository.getAllUrls(screen.id))
        }
        var selectedImageIndex by rememberRetained {
            mutableStateOf(NO_IMAGE_SELECTED_INDEX)
        }
        val title by rememberRetained(selectedImageIndex) {
            mutableStateOf(
                if (selectedImageIndex == NO_IMAGE_SELECTED_INDEX) {
                    "Cover arts"
                } else {
                    val imageUrl = imageUrls[selectedImageIndex]
                    imageUrl.types.joinToString().appendOptionalText(imageUrl.comment)
                },
            )
        }

        fun eventSink(event: CoverArtsUiEvent) {
            when (event) {
                CoverArtsUiEvent.NavigateUp -> {
                    if (selectedImageIndex == NO_IMAGE_SELECTED_INDEX) {
                        navigator.pop()
                    } else {
                        selectedImageIndex = NO_IMAGE_SELECTED_INDEX
                    }
                }

                is CoverArtsUiEvent.SelectImage -> {
                    selectedImageIndex = event.index
                }
            }
        }

        return CoverArtsUiState(
            id = screen.id,
            title = title,
            imageUrls = imageUrls,
            selectedImageIndex = selectedImageIndex,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsUiState(
    val id: String,
    val title: String = "",
    val imageUrls: List<ImageUrls>,
    val selectedImageIndex: Int = NO_IMAGE_SELECTED_INDEX,
    val eventSink: (CoverArtsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsUiEvent

    data class SelectImage(val index: Int) : CoverArtsUiEvent
}
