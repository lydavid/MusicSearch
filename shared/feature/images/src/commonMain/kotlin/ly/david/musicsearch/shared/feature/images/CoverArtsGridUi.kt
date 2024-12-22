package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.screen.screenContainerSize
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.image.ThumbnailImage
import kotlin.math.roundToInt

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsGridUi(
    state: CoverArtsGridUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val lazyGridState = rememberLazyGridState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(CoverArtsGridUiEvent.NavigateUp)
                },
                title = state.title,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                },
            )
        },
    ) { innerPadding ->
        val imageUrls = state.imageUrls.toImmutableList()
        CoverArtsGrid(
            imageUrls = imageUrls,
            onImageClick = {
                eventSink(CoverArtsGridUiEvent.SelectImage(it))
            },
            modifier = Modifier.padding(innerPadding),
            lazyGridState = lazyGridState,
        )
    }
}

private const val GRID_SIZE = 4

@Composable
internal fun CoverArtsGrid(
    imageUrls: ImmutableList<ImageUrls>,
    onImageClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
) {
    val density: Density = LocalDensity.current
    val screenWidth: Int = screenContainerSize().width
    val columnWidth: Int = (screenWidth / GRID_SIZE.toDouble()).roundToInt()
    val size: Dp = remember(density, screenWidth) {
        with(density) { columnWidth.toDp() }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(GRID_SIZE),
        state = lazyGridState,
        modifier = modifier,
    ) {
        items(imageUrls.size) { index ->
            val imageUrl = imageUrls[index]

            // Because the number of images displayed can change when we filter
            // the placeholder key must not depend on the index of the initial set of images
            ThumbnailImage(
                url = imageUrl.thumbnailUrl,
                placeholderKey = imageUrl.databaseId.toString(),
                placeholderIcon = MusicBrainzEntity.RELEASE.getIcon(),
                size = size,
                modifier = Modifier.clickable {
                    onImageClick(index)
                },
            )
        }
    }
}
