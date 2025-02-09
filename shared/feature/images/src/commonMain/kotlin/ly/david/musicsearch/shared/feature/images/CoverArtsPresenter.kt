package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.PagingData
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class CoverArtsPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val releaseImageRepository: ReleaseImageRepository,
    private val getMusicBrainzCoverArtUrl: GetMusicBrainzCoverArtUrl,
) : Presenter<CoverArtsUiState> {

    @Composable
    override fun present(): CoverArtsUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val imageMetadataFlow: Flow<PagingData<ImageMetadata>> by rememberRetained(topAppBarFilterState.filterText) {
            mutableStateOf(
                releaseImageRepository.observeAllImageMetadata(
                    mbid = screen.id,
                    query = topAppBarFilterState.filterText,
                ),
            )
        }
        val lazyGridState: LazyGridState = rememberLazyGridState()
        var selectedIndex: Int? by rememberRetained {
            mutableStateOf(null)
        }
        var imageMetadataListSnapshot: ImmutableList<ImageMetadata?> by rememberRetained {
            mutableStateOf(persistentListOf())
        }

        val selectedImageMetadata: ImageMetadata? by remember(
            selectedIndex,
            imageMetadataListSnapshot,
        ) {
            val capturedSelectedImageIndex = selectedIndex
            mutableStateOf(
                if (capturedSelectedImageIndex == null || imageMetadataListSnapshot.isEmpty()) {
                    null
                } else {
                    imageMetadataListSnapshot[capturedSelectedImageIndex]
                },
            )
        }

        val title by remember(
            selectedImageMetadata,
        ) {
            val index = selectedIndex
            mutableStateOf(
                if (selectedImageMetadata == null || index == null) {
                    "Cover arts"
                } else {
                    val imageMetadata = selectedImageMetadata
                    val pages = "${index + 1}/${imageMetadataListSnapshot.size}"
                    val typeAndComment =
                        imageMetadata?.types?.joinToString()?.appendOptionalText(imageMetadata.comment).orEmpty()
                    "[$pages]".appendOptionalText(typeAndComment)
                },
            )
        }

        val subtitle by remember(
            selectedImageMetadata,
        ) {
            mutableStateOf(selectedImageMetadata?.getNameWithDisambiguation().orEmpty())
        }

        val url by remember(
            selectedImageMetadata,
        ) {
            mutableStateOf(
                selectedImageMetadata?.largeUrl ?: screen.id?.let { getMusicBrainzCoverArtUrl(it) },
            )
        }

        topAppBarFilterState.show(selectedIndex == null)

        fun eventSink(event: CoverArtsUiEvent) {
            when (event) {
                CoverArtsUiEvent.NavigateUp -> {
                    if (selectedIndex == null) {
                        navigator.pop()
                    } else {
                        selectedIndex = null
                        if (topAppBarFilterState.filterText.isNotEmpty()) {
                            topAppBarFilterState.toggleFilterMode(true)
                        }
                    }
                }

                is CoverArtsUiEvent.SelectImage -> {
                    selectedIndex = event.index
                    if (event.imageMetadataSnapshot.isEmpty()) {
                        println("do something, this is a signal that we returned to this screen but data has changed")
                    } else {
                        imageMetadataListSnapshot = event.imageMetadataSnapshot.toImmutableList()
                    }
                    topAppBarFilterState.toggleFilterMode(false)
                }

                is CoverArtsUiEvent.ClickItem -> {
                    navigator.goTo(
                        DetailsScreen(
                            entity = event.entity,
                            id = event.id,
                        ),
                    )
                }
            }
        }

        return CoverArtsUiState(
            title = title,
            subtitle = subtitle,
            url = url,
            selectedImageIndex = selectedIndex,
            selectedImageMetadata = selectedImageMetadata,
            imageMetadataPagingDataFlow = imageMetadataFlow,
            lazyGridState = lazyGridState,
            topAppBarFilterState = topAppBarFilterState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CoverArtsUiState(
    val title: String = "",
    val subtitle: String = "",
    val url: String? = null,
    val imageMetadataPagingDataFlow: Flow<PagingData<ImageMetadata>>,
    val lazyGridState: LazyGridState = LazyGridState(),
    val selectedImageIndex: Int? = null,
    val selectedImageMetadata: ImageMetadata? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val eventSink: (CoverArtsUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface CoverArtsUiEvent : CircuitUiEvent {
    data object NavigateUp : CoverArtsUiEvent

    data class SelectImage(
        val index: Int,
        val imageMetadataSnapshot: List<ImageMetadata?>,
    ) : CoverArtsUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
    ) : CoverArtsUiEvent
}
