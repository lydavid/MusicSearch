package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CoverArtsGridPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
    private val getMusicBrainzCoverArtUrl: GetMusicBrainzCoverArtUrl,
) : Presenter<CoverArtsGridUiState> {

    @Composable
    override fun present(): CoverArtsGridUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val imageUrlsFlow: Flow<PagingData<ImageUrls>> by rememberRetained(topAppBarFilterState.filterText) {
            mutableStateOf(
                releaseImageRepository.observeAllImageUrls(
                    mbid = screen.id,
                    query = topAppBarFilterState.filterText,
                ),
            )
        }
        val imageUrls: LazyPagingItems<ImageUrls> = imageUrlsFlow.collectAsLazyPagingItems()
        val lazyGridState: LazyGridState = rememberLazyGridState()
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
                    val typeAndComment = imageUrl?.types?.joinToString()?.appendOptionalText(imageUrl.comment).orEmpty()
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
                    "${capturedSelectedImageIndex + 1} / ${imageUrls.itemCount}"
                },
            )
        }

        val url by rememberSaveable(
            selectedImageIndex,
        ) {
            val capturedSelectedImageIndex = selectedImageIndex
            mutableStateOf(
                if (capturedSelectedImageIndex == null) {
                    screen.id?.let { getMusicBrainzCoverArtUrl(it) }
                } else {
                    imageUrls[capturedSelectedImageIndex]?.largeUrl
                }
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
            title = title,
            subtitle = subtitle,
            url = url,
            selectedImageIndex = selectedImageIndex,
            imageUrls = imageUrlsFlow.collectAsLazyPagingItems(),
            lazyGridState = lazyGridState,
            topAppBarFilterState = topAppBarFilterState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsGridUiState(
    val title: String = "",
    val subtitle: String = "",
    val url: String? = null,
    val imageUrls: LazyPagingItems<ImageUrls>,
    val lazyGridState: LazyGridState = LazyGridState(),
    val selectedImageIndex: Int? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val eventSink: (CoverArtsGridUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsGridUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsGridUiEvent

    data class UpdateQuery(val query: String) : CoverArtsGridUiEvent

    data class SelectImage(
        val index: Int,
    ) : CoverArtsGridUiEvent
}
