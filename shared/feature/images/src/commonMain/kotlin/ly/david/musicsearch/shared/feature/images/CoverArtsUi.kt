package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun CoverArtsUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()

    CoverArtsUi(
        state = state,
        modifier = modifier,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
) {
    val eventSink = state.eventSink
    val lazyGridState = rememberLazyGridState()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = {
                    eventSink(CoverArtsUiEvent.NavigateUp)
                },
                title = state.title,
            )
        },
    ) { innerPadding ->
        val imageUrls = state.imageUrls.toImmutableList()
        val selectedImageIndex = state.selectedImageIndex
        if (selectedImageIndex == NO_IMAGE_SELECTED_INDEX) {
            CoverArtsGrid(
                mbid = state.id,
                imageUrls = imageUrls,
                modifier = Modifier.padding(innerPadding),
                lazyGridState = lazyGridState,
                onImageClick = {
                    eventSink(CoverArtsUiEvent.SelectImage(it))
                },
            )
        } else {
            CoverArtsPager(
                mbid = state.id,
                imageUrls = imageUrls,
                selectedImageIndex = selectedImageIndex,
                modifier = Modifier.padding(innerPadding),
                isCompact = isCompact,
                onImageChange = {
                    eventSink(CoverArtsUiEvent.SelectImage(it))
                },
            )
        }
    }
}
