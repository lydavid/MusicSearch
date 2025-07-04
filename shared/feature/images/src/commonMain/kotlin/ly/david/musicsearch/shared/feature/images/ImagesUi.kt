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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.DEFAULT_IMAGES_GRID_PADDING_DP
import ly.david.musicsearch.shared.domain.DEFAULT_NUMBER_OF_IMAGES_PER_ROW
import ly.david.musicsearch.shared.domain.common.appendOptionalText
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.component.MultipleChoiceBottomSheet
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.screen.screenContainerSize
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.theme.LocalStrings

@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
internal fun ImagesUi(
    state: ImagesUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    BackHandler {
        eventSink(ImagesUiEvent.NavigateUp)
    }

    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()
    ImagesUi(
        state = state,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun ImagesUi(
    state: ImagesUiState,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val eventSink = state.eventSink
    val imageMetadataLazyPagingItems: LazyPagingItems<ImageMetadata> = state
        .imageMetadataPagingDataFlow
        .collectAsLazyPagingItems()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()

    if (showBottomSheet) {
        MultipleChoiceBottomSheet(
            options = ImagesSortOption.entries.map { it.getLabel(strings) },
            selectedOptionIndex = state.sortOption.ordinal,
            onSortOptionIndexClick = {
                eventSink(ImagesUiEvent.UpdateSortOption(it))
            },
            bottomSheetState = bottomSheetState,
            onDismiss = { showBottomSheet = false },
        )
    }

    val title = when (val title = state.title) {
        is ImagesTitle.All -> strings.images
        is ImagesTitle.Selected -> {
            val pages = "${title.page}/${title.totalPages}"
            "[$pages]".appendOptionalText(title.typeAndComment)
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(ImagesUiEvent.NavigateUp)
                },
                title = title,
                subtitle = state.subtitle,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    state.url?.let { url ->
                        OpenInBrowserMenuItem(
                            url = url,
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(strings.sort) },
                        onClick = {
                            showBottomSheet = true
                            closeMenu()
                        },
                    )
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
                                ImagesUiEvent.ClickItem(
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

        val capturedSelectedImageIndex = state.selectedImageIndex

        if (capturedSelectedImageIndex == null) {
            CoverArtsGrid(
                imageMetadataList = imageMetadataLazyPagingItems,
                onImageClick = { index ->
                    val snapshot = imageMetadataLazyPagingItems
                        .itemSnapshotList
                    eventSink(
                        ImagesUiEvent.SelectImage(
                            index,
                            snapshot,
                        ),
                    )
                },
                lazyGridState = state.lazyGridState,
                modifier = Modifier.padding(innerPadding),
                numberOfImagesPerRow = state.numberOfImagesPerRow,
                imagesGridPaddingDp = state.imagesGridPaddingDp,
            )
        } else {
            CoverArtsPager(
                imageMetadataList = imageMetadataLazyPagingItems,
                selectedImageIndex = capturedSelectedImageIndex,
                isCompact = isCompact,
                onImageChange = { index ->
                    val snapshot = imageMetadataLazyPagingItems
                        .itemSnapshotList
                    eventSink(
                        ImagesUiEvent.SelectImage(
                            index,
                            snapshot,
                        ),
                    )
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
private fun CoverArtsGrid(
    imageMetadataList: LazyPagingItems<ImageMetadata>,
    onImageClick: (index: Int) -> Unit,
    lazyGridState: LazyGridState,
    modifier: Modifier = Modifier,
    numberOfImagesPerRow: Int = DEFAULT_NUMBER_OF_IMAGES_PER_ROW,
    imagesGridPaddingDp: Int = DEFAULT_IMAGES_GRID_PADDING_DP,
) {
    val density: Density = LocalDensity.current
    val screenWidth: Int = screenContainerSize().width
    val paddingWidthPx = with(density) { imagesGridPaddingDp.dp.roundToPx() }
    val totalInnerPaddingWidth = (numberOfImagesPerRow - 1) * paddingWidthPx
    val imageSizePx = (screenWidth - totalInnerPaddingWidth) / numberOfImagesPerRow
    val imageSize = with(density) { imageSizePx.toDp() }

    LazyVerticalGrid(
        columns = GridCells.Fixed(numberOfImagesPerRow),
        modifier = modifier,
        state = lazyGridState,
        horizontalArrangement = Arrangement.spacedBy(imagesGridPaddingDp.dp),
        verticalArrangement = Arrangement.spacedBy(imagesGridPaddingDp.dp),
    ) {
        items(
            count = imageMetadataList.itemCount,
            key = imageMetadataList.itemKey { it.imageId.value },
            contentType = { ImageMetadata() },
        ) { index ->
            imageMetadataList[index]?.let { imageMetadata ->
                // Because the number of images displayed can change when we filter
                // the placeholder key must not depend on the index of the initial set of images
                ThumbnailImage(
                    url = imageMetadata.thumbnailUrl,
                    imageId = imageMetadata.imageId,
                    placeholderIcon = imageMetadata.entity?.getIcon(),
                    size = imageSize,
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
                        imageId = imageMetadata.imageId,
                        isCompact = isCompact,
                        zoomEnabled = true,
                    )
                }
            }
        }
    }
}
