package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithEntity
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class ImagesPresenter(
    private val screen: CoverArtsScreen,
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val musicBrainzImageMetadataRepository: MusicBrainzImageMetadataRepository,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) : Presenter<ImagesUiState> {

    @Composable
    override fun present(): ImagesUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val sortOption by appPreferences.imagesSortOption.collectAsState(ImagesSortOption.RECENTLY_ADDED)
        val numberOfImagesPerRow by appPreferences.observeNumberOfImagesPerRow.collectAsState(
            initial = DEFAULT_NUMBER_OF_IMAGES_PER_ROW,
        )
        val imagesGridPaddingDp by appPreferences.observeImagesGridPaddingDp.collectAsState(
            initial = DEFAULT_IMAGES_GRID_PADDING_DP,
        )
        val imageMetadataFlow: Flow<PagingData<ImageMetadataWithEntity>> by rememberRetained(
            topAppBarFilterState.filterText,
            sortOption,
        ) {
            mutableStateOf(
                musicBrainzImageMetadataRepository.observeAllImageMetadata(
                    mbid = screen.entity?.id,
                    query = topAppBarFilterState.filterText,
                    sortOption = sortOption,
                ),
            )
        }
        val lazyGridState: LazyGridState = rememberLazyGridState()
        var selectedIndex: Int? by rememberRetained {
            mutableStateOf(null)
        }
        var isSingleImage: Boolean by rememberRetained {
            mutableStateOf(false)
        }
        var imageMetadataListSnapshot: ImmutableList<ImageMetadataWithEntity?> by rememberRetained {
            mutableStateOf(persistentListOf())
        }

        val selectedImageMetadataWithEntity: ImageMetadataWithEntity? by rememberRetained(
            selectedIndex,
            imageMetadataListSnapshot,
        ) {
            val capturedSelectedImageIndex = selectedIndex
            derivedStateOf {
                if (capturedSelectedImageIndex == null || imageMetadataListSnapshot.isEmpty()) {
                    null
                } else {
                    imageMetadataListSnapshot[capturedSelectedImageIndex]
                }
            }
        }

        val title by rememberRetained(
            selectedImageMetadataWithEntity,
            selectedIndex,
            imageMetadataListSnapshot,
        ) {
            val index = selectedIndex
            derivedStateOf {
                if (selectedImageMetadataWithEntity == null || index == null) {
                    ImagesTitle.All
                } else {
                    val typeAndComment = when (val imageMetadata = selectedImageMetadataWithEntity?.imageMetadata) {
                        is ImageMetadata.InternetArchive -> {
                            imageMetadata.types.joinToString().appendOptionalText(imageMetadata.comment)
                        }
                        else -> ""
                    }

                    ImagesTitle.Selected(
                        page = index + 1,
                        totalPages = imageMetadataListSnapshot.size,
                        typeAndComment = typeAndComment,
                    )
                }
            }
        }

        val subtitle = selectedImageMetadataWithEntity?.getNameWithDisambiguation().orEmpty()

        val linkUrl: String by rememberRetained(
            selectedImageMetadataWithEntity,
        ) {
            derivedStateOf {
                getImageLinkUrl(
                    imageMetadata = selectedImageMetadataWithEntity?.imageMetadata,
                    entity = screen.entity ?: selectedImageMetadataWithEntity?.musicBrainzEntity,
                )
            }
        }

        topAppBarFilterState.show(selectedIndex == null)

        fun eventSink(event: ImagesUiEvent) {
            when (event) {
                ImagesUiEvent.NavigateUp -> {
                    if (selectedIndex == null || isSingleImage) {
                        navigator.pop()
                    } else {
                        selectedIndex = null
                        if (topAppBarFilterState.filterText.isNotEmpty()) {
                            topAppBarFilterState.toggleFilterMode(true)
                        }
                    }
                }

                is ImagesUiEvent.SelectImage -> {
                    selectedIndex = event.index
                    if (event.imageMetadataSnapshot.isEmpty()) {
                        println("do something, this is a signal that we returned to this screen but data has changed")
                    } else {
                        imageMetadataListSnapshot = event.imageMetadataSnapshot.toImmutableList()
                    }
                    topAppBarFilterState.toggleFilterMode(false)
                }

                is ImagesUiEvent.AutoSelectSingleImage -> {
                    selectedIndex = 0
                    isSingleImage = true
                }

                is ImagesUiEvent.ClickItem -> {
                    navigator.goTo(
                        DetailsScreen(
                            entityType = event.entityType,
                            id = event.id,
                        ),
                    )
                }

                is ImagesUiEvent.UpdateSortOption -> {
                    appPreferences.setImagesSortOption(ImagesSortOption.entries[event.sortOptionIndex])
                }
            }
        }

        return ImagesUiState(
            title = title,
            subtitle = subtitle,
            linkUrl = linkUrl,
            numberOfImagesPerRow = numberOfImagesPerRow,
            imagesGridPaddingDp = imagesGridPaddingDp,
            selectedImageIndex = selectedIndex,
            selectedImageMetadata = selectedImageMetadataWithEntity,
            imageMetadataPagingDataFlow = imageMetadataFlow,
            lazyGridState = lazyGridState,
            sortOption = sortOption,
            topAppBarFilterState = topAppBarFilterState,
            showSort = screen.entity?.id == null && selectedIndex == null,
            eventSink = ::eventSink,
        )
    }

    private fun getImageLinkUrl(
        entity: MusicBrainzEntity?,
        imageMetadata: ImageMetadata?,
    ): String {
        return when {
            entity == null -> { // all images screen
                ""
            }
            imageMetadata == null -> { // no image selected in an entity's images screen
                getMusicBrainzCoverArtUrl(entity = entity)
            }
            else -> {
                imageMetadata.linkUrl
            }
        }
    }

    private fun getMusicBrainzCoverArtUrl(
        entity: MusicBrainzEntity,
    ): String {
        val entityUrl = "${getMusicBrainzUrl(entity = entity)}/"
        return when (entity.type) {
            MusicBrainzEntityType.EVENT -> entityUrl + "event-art"
            MusicBrainzEntityType.RELEASE -> entityUrl + "cover-art"
            else -> entityUrl
        }
    }
}

internal sealed class ImagesTitle {
    data object All : ImagesTitle()
    data class Selected(
        val page: Int,
        val totalPages: Int,
        val typeAndComment: String,
    ) : ImagesTitle()
}

@Stable
internal data class ImagesUiState(
    val title: ImagesTitle = ImagesTitle.All,
    val subtitle: String = "",
    val linkUrl: String = "",
    val numberOfImagesPerRow: Int = DEFAULT_NUMBER_OF_IMAGES_PER_ROW,
    val imagesGridPaddingDp: Int = DEFAULT_IMAGES_GRID_PADDING_DP,
    val imageMetadataPagingDataFlow: Flow<PagingData<ImageMetadataWithEntity>>,
    val lazyGridState: LazyGridState = LazyGridState(),
    val sortOption: ImagesSortOption = ImagesSortOption.RECENTLY_ADDED,
    val selectedImageIndex: Int? = null,
    val selectedImageMetadata: ImageMetadataWithEntity? = null,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val showSort: Boolean = false,
    val eventSink: (ImagesUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface ImagesUiEvent : CircuitUiEvent {
    data object NavigateUp : ImagesUiEvent

    data class SelectImage(
        val index: Int,
        val imageMetadataSnapshot: List<ImageMetadataWithEntity?>,
    ) : ImagesUiEvent

    data object AutoSelectSingleImage : ImagesUiEvent

    data class ClickItem(
        val entityType: MusicBrainzEntityType,
        val id: String,
    ) : ImagesUiEvent

    data class UpdateSortOption(
        val sortOptionIndex: Int,
    ) : ImagesUiEvent
}
