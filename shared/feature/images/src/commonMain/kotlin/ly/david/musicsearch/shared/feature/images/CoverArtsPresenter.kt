package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen

internal class CoverArtsPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<CoverArtsUiState> {

    @Composable
    override fun present(): CoverArtsUiState {
        var imageUrls by rememberSaveable { mutableStateOf<List<ImageUrls>>(listOf()) }

        LaunchedEffect(Unit) {
            imageUrls = releaseImageRepository.getAllUrls(screen.id)
        }

        fun eventSink(event: CoverArtsUiEvent) {
            when (event) {
                CoverArtsUiEvent.NavigateUp -> {
                    navigator.pop()
                }
            }
        }

        return CoverArtsUiState(
            id = screen.id,
            imageUrls = imageUrls,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsUiState(
    val id: String,
    val imageUrls: List<ImageUrls>,
    val eventSink: (CoverArtsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsUiEvent
}
