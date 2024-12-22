package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.image.LargeImage

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun CoverArtsPagerUi(
    state: CoverArtsPagerUiState,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass()

    CoverArtsPagerUi(
        state = state,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsPagerUi(
    state: CoverArtsPagerUiState,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = {
                    eventSink(CoverArtsPagerUiEvent.NavigateUp)
                },
                title = state.title,
                subtitle = state.subtitle,
            )
        },
    ) { innerPadding ->
        val imageUrls = state.imageUrls.toImmutableList()
        val selectedImageIndex = state.selectedImageIndex
        CoverArtsPager(
            imageUrls = imageUrls,
            selectedImageIndex = selectedImageIndex,
            isCompact = isCompact,
            onImageChange = {
                eventSink(CoverArtsPagerUiEvent.ChangeImage(it))
            },
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Composable
private fun CoverArtsPager(
    imageUrls: ImmutableList<ImageUrls>,
    selectedImageIndex: Int,
    isCompact: Boolean,
    onImageChange: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()

    val latestOnImageChange by rememberUpdatedState(onImageChange)
    val pagerState = rememberPagerState(
        initialPage = selectedImageIndex,
        pageCount = { imageUrls.size },
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
            val imageUrl = imageUrls[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LargeImage(
                    url = imageUrl.largeUrl,
                    placeholderKey = imageUrl.databaseId.toString(),
                    isCompact = isCompact,
                )
            }
        }
    }
}
