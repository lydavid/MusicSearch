package ly.david.musicsearch.ui.common.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.Header
import ly.david.musicsearch.ui.common.button.RetryButton
import ly.david.musicsearch.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.musicsearch.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.theme.LocalStrings

/**
 * Handles loading and errors for paging screens.
 *
 * Also handles swipe to refresh. The source for [lazyPagingItems] is expected to implement refresh behaviour.
 * This can be done using [BrowseEntityRemoteMediator] or [LookupEntityRemoteMediator].
 *
 * @param lazyPagingItems The items to display. Their [Identifiable.id] must be unique.
 *  This is already true for any item that uses MB's UUID, but separators must also have a unique id.
 * @param modifier For lazy column containing [itemContent].
 *
 *  @param itemContent Composable UI for each [lazyPagingItems].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Identifiable> ScreenWithPagingLoadingAndError(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    customNoResultsText: String = "",
    keyed: Boolean = false,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
    val strings = LocalStrings.current
    val noResultsText = customNoResultsText.ifEmpty {
        strings.noResultsFound
    }
    val isRefreshing = lazyPagingItems.loadState.refresh == LoadStateLoading
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = lazyPagingItems::refresh,
    ) {
        // This doesn't affect "loads" from db/source.
        when {
            lazyPagingItems.loadState.refresh is LoadStateLoading -> {
                FullScreenLoadingIndicator()
            }

            lazyPagingItems.loadState.refresh is LoadStateError -> {
                FullScreenErrorWithRetry(
                    handledException = HandledException(
                        userMessage = (lazyPagingItems.loadState.refresh as LoadStateError).error.message.orEmpty(),
                        errorResolution = ErrorResolution.Retry,
                    ),
                    onClick = { lazyPagingItems.refresh() },
                )
            }

            lazyPagingItems.loadState.append.endOfPaginationReached &&
                lazyPagingItems.itemCount == 0 ||
                lazyPagingItems.itemCount == 1 && lazyPagingItems[0] is Header -> {
                FullScreenText(noResultsText)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        // TODO: do not set keyed to true without resolving the issue with footer
                        //  sometimes showing up before the rest and causing it to stay as the first visible item.
                        key = lazyPagingItems.itemKey { it.id }.takeIf { keyed },
                        contentType = { lazyPagingItems[it] },
                    ) { index ->
                        itemContent(lazyPagingItems[index])

                        // Is this inefficient? At least it preserves list state on configuration change.
                        if (index == lazyPagingItems.itemCount - 1) {
                            when (lazyPagingItems.loadState.append) {
                                is LoadStateLoading -> {
                                    FooterLoadingIndicator()
                                }

                                is LoadStateError -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        RetryButton(onClick = { lazyPagingItems.refresh() })
                                    }
                                }

                                else -> {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
