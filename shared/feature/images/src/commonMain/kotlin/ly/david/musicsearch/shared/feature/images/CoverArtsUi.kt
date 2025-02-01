package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.slack.circuit.foundation.internal.BackHandler
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.screen.screenContainerSize
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.image.LargeImage
import ly.david.musicsearch.ui.image.ThumbnailImage
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun CoverArtsGridUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    BackHandler {
        eventSink(CoverArtsUiEvent.NavigateUp)
    }

    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
    CoverArtsGridUi(
        state = state,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsGridUi(
    state: CoverArtsUiState,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(CoverArtsUiEvent.NavigateUp)
                },
                title = state.title,
                subtitle = state.subtitle,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    state.url?.let { url ->
                        OpenInBrowserMenuItem(
                            url = url,
                        )
                    }
                },
                subtitleDropdownMenuItems = {
                    val name = state.selectedImageMetadata?.name ?: return@TopAppBarWithFilter
                    val entity = state.selectedImageMetadata.entity ?: return@TopAppBarWithFilter
                    val id = state.selectedImageMetadata.mbid ?: return@TopAppBarWithFilter

                    DropdownMenuItem(
                        text = { Text(name) },
                        leadingIcon = { EntityIcon(entity = entity) },
                        onClick = {
                            closeMenu()
                            eventSink(
                                CoverArtsUiEvent.ClickItem(
                                    entity = entity,
                                    id = id,
                                ),
                            )
                        },
                    )
                },
            )
        },
    ) { innerPadding ->
        val imageUrls = state.imageMetadataList
        val capturedSelectedImageIndex = state.selectedImageIndex
        if (capturedSelectedImageIndex == null) {
            CoverArtsGrid(
                imageMetadataList = imageUrls,
                onImageClick = { index ->
                    eventSink(CoverArtsUiEvent.SelectImage(index))
                },
                lazyGridState = state.lazyGridState,
                modifier = Modifier.padding(innerPadding),
            )
        } else {
            CoverArtsPager(
                imageMetadataList = imageUrls,
                selectedImageIndex = capturedSelectedImageIndex,
                isCompact = isCompact,
                onImageChange = { index ->
                    eventSink(CoverArtsUiEvent.SelectImage(index))
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

private const val GRID_SIZE = 4

@Composable
private fun CoverArtsGrid(
    imageMetadataList: LazyPagingItems<ImageMetadata>,
    onImageClick: (index: Int) -> Unit,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
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
        items(
            count = imageMetadataList.itemCount,
            key = imageMetadataList.itemKey { it.databaseId },
            contentType = { ImageMetadata() },
        ) { index ->
            imageMetadataList[index]?.let { imageUrl ->
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
}

@Composable
private fun CoverArtsPager(
    imageMetadataList: LazyPagingItems<ImageMetadata>,
    selectedImageIndex: Int,
    isCompact: Boolean,
    onImageChange: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val latestOnImageChange by rememberUpdatedState(onImageChange)
    val pagerState = rememberPagerState(
        initialPage = selectedImageIndex,
        pageCount = { imageMetadataList.itemCount },
    )
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            latestOnImageChange(page)
        }
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyUp) {
                    when (it.key) {
                        Key.DirectionLeft -> {
                            coroutineScope.launch {
                                pagerState.scrollToPage(pagerState.currentPage - 1)
                            }
                            true
                        }

                        Key.DirectionRight -> {
                            coroutineScope.launch {
                                pagerState.scrollToPage(pagerState.currentPage + 1)
                            }
                            true
                        }

                        else -> false
                    }
                } else {
                    // Let other handlers receive this event
                    false
                }
            }
            .focusRequester(focusRequester)
            .focusable(),
    ) {
        HorizontalPager(
            state = pagerState,
            beyondViewportPageCount = 1,
        ) { page ->
            imageMetadataList[page]?.let { imageMetadata ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LargeImage(
                        url = imageMetadata.largeUrl,
                        placeholderKey = imageMetadata.databaseId.toString(),
                        isCompact = isCompact,
                        zoomEnabled = true,
                    )
                }
            }
        }
    }
}
