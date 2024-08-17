package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
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
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsUi(
    modifier: Modifier = Modifier,
    isCompact: Boolean = false,
    imageUrlsList: ImmutableList<ImageUrls> = persistentListOf(),
    onBack: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { imageUrlsList.size })

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
                .fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1,
            ) { page ->
                val url = imageUrlsList[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LargeImage(
                        url = url.largeUrl,
                        id = url.largeUrl,
                        modifier = Modifier,
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
