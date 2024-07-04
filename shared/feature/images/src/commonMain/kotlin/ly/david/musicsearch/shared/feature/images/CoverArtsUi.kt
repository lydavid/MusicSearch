package ly.david.musicsearch.shared.feature.images

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import ly.david.musicsearch.ui.image.LargeImage

@Composable
internal fun CoverArtsUi(
    state: CoverArtsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    CoverArtsUi(
        imageUrlsList = state.imageUrls.toImmutableList(),
        onBack = {
            eventSink(CoverArtsUiEvent.NavigateUp)
        },
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun CoverArtsUi(
    modifier: Modifier = Modifier,
    imageUrlsList: ImmutableList<ImageUrls> = persistentListOf(),
    onBack: () -> Unit = {},
) {
    val pagerState = rememberPagerState(pageCount = { imageUrlsList.size })
//    val scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                onBack = onBack,
                title = " ",
//                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier,
                beyondBoundsPageCount = 1,
//                    .verticalScroll(rememberScrollState())
//                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) { page ->
                val url = imageUrlsList[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                ) {
                    LargeImage(
                        url = url.largeUrl,
                        id = url.largeUrl,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "${pagerState.currentPage + 1} / ${pagerState.pageCount}",
                )
            }
        }
    }
}
