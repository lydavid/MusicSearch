package ly.david.mbjc.ui.experimental.nowplaying

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import ly.david.data.domain.listitem.NowPlayingHistoryListItemModel
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.rememberFlowWithLifecycleStarted

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NowPlayingHistoryScreen(
    viewModel: NowPlayingViewModel = hiltViewModel(),
) {
    val lazyPagingItems = rememberFlowWithLifecycleStarted(viewModel.nowPlayingHistory)
        .collectAsLazyPagingItems()

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
    ) { nowPlayingHistory: NowPlayingHistoryListItemModel? ->
        when (nowPlayingHistory) {
            is NowPlayingHistoryListItemModel -> {
                NowPlayingCard(
                    nowPlayingHistory = nowPlayingHistory,
                    modifier = Modifier.animateItemPlacement()
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
