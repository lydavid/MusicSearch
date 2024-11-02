package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.image.LargeImage

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal fun CoverArtsUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
) {
    val windowSizeClass = calculateWindowSizeClass()
    val eventSink = state.eventSink

    CoverArtsUi(
        modifier = modifier,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        imageUrlsList = state.imageUrls.toImmutableList(),
        onBack = {
            eventSink(CoverArtsUiEvent.NavigateUp)
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsUi(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    imageUrlsList: ImmutableList<ImageUrls> = persistentListOf(),
    onBack: () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { imageUrlsList.size })
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = onBack,
                title = " ",
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
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
                val url = imageUrlsList[page]
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LargeImage(
                        url = url.largeUrl,
                        id = url.largeUrl,
                        isCompact = isCompact,
                    )
                }
            }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",
                )
            }
        }
    }
}
