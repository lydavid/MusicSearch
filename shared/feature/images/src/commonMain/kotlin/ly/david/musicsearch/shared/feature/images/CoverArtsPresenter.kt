package ly.david.musicsearch.shared.feature.images

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsGridScreen
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
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val imageUrls by rememberRetained(topAppBarFilterState.filterText) {
            mutableStateOf(
                releaseImageRepository.getAllUrlsById(
                    mbid = screen.id,
                    query = topAppBarFilterState.filterText,
                ),
            )
        }
        var selectedImageIndex: Int? by remember {
            mutableStateOf(null)
        }
        val title by rememberRetained(
            selectedImageIndex,
            topAppBarFilterState.filterText,
        ) {
            val capturedSelectedImageIndex = selectedImageIndex
            mutableStateOf(
                if (capturedSelectedImageIndex == null) {
                    "Cover arts"
                } else {
                    val imageUrl = imageUrls[capturedSelectedImageIndex]
                    val typeAndComment = imageUrl.types.joinToString().appendOptionalText(imageUrl.comment)
                    typeAndComment.ifEmpty { " " }
                },
            )
        }
        val subtitle by rememberRetained(
            selectedImageIndex,
            topAppBarFilterState.filterText,
        ) {
            val capturedSelectedImageIndex = selectedImageIndex
            mutableStateOf(
                if (capturedSelectedImageIndex == null) {
                    ""
                } else {
                    "${capturedSelectedImageIndex + 1} / ${imageUrls.size}"
                },
            )
        }

        fun eventSink(event: CoverArtsGridUiEvent) {
            when (event) {
                CoverArtsGridUiEvent.NavigateUp -> {
                    if (selectedImageIndex == null) {
                        navigator.pop()
                    } else {
                        selectedImageIndex = null
                        if (topAppBarFilterState.filterText.isNotEmpty()) {
                            topAppBarFilterState.toggleFilterMode(true)
                        }
                        topAppBarFilterState.show(true)
                    }
                }

                is CoverArtsGridUiEvent.UpdateQuery -> {
                    topAppBarFilterState.updateFilterText(event.query)
                }

                is CoverArtsGridUiEvent.SelectImage -> {
                    selectedImageIndex = event.index
                    topAppBarFilterState.toggleFilterMode(false)
                    topAppBarFilterState.show(false)
                }
            }
        }

        return CoverArtsGridUiState(
            id = screen.id,
            title = title,
            subtitle = subtitle,
            url = getMusicBrainzCoverArtUrl(screen.id),
            selectedImageIndex = selectedImageIndex,
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
    val subtitle: String = "",
    val url: String = "",
    val imageUrls: List<ImageUrls>,
    val selectedImageIndex: Int? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val eventSink: (CoverArtsGridUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsGridUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsGridUiEvent

    data class UpdateQuery(val query: String) : CoverArtsGridUiEvent

    data class SelectImage(val index: Int) : CoverArtsGridUiEvent
}
