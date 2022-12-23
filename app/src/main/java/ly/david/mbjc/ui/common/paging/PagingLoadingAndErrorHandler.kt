package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.RetryButton
import ly.david.mbjc.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.fullscreen.FullScreenText

/**
 * Handles loading and errors for paging screens.
 *
 * Also handles swipe to refresh. The source for [lazyPagingItems] is expected to implement refresh behaviour.
 * This can be done using one of [BrowseResourceRemoteMediator] or [LookupResourceRemoteMediator].
 *
 * @param modifier For lazy column containing [itemContent].
 * @param somethingElseLoading Whether something else is loading, in which case this should present a loading state.
 *  Although this should be decoupled, some screens' paged contents relies on a lookup that feeds data into Room,
 *  which is then loaded with pagination.
 *
 *  @param itemContent Composable UI for each [lazyPagingItems].
 */
@Composable
internal fun <T : Any> PagingLoadingAndErrorHandler(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    somethingElseLoading: Boolean = false,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState? = null,
    noResultsText: String = stringResource(id = R.string.no_results_found),
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {

    // This doesn't affect "loads" from db/source.
    when {
        lazyPagingItems.loadState.refresh is LoadState.Loading || somethingElseLoading -> {
            FullScreenLoadingIndicator()
        }
        lazyPagingItems.loadState.refresh is LoadState.Error -> {
            // TODO: going to another tab, and coming back will show same error message (doesn't make another call)
            LaunchedEffect(Unit) {
                val errorMessage = (lazyPagingItems.loadState.refresh as LoadState.Error).error.message
                val displayMessage = "Failed to fetch data: ${errorMessage ?: "unknown"}"
                snackbarHostState?.showSnackbar(displayMessage)
            }

            FullScreenErrorWithRetry(onClick = { lazyPagingItems.refresh() })
        }
        lazyPagingItems.loadState.append.endOfPaginationReached && lazyPagingItems.itemCount == 0 -> {
            // TODO: cannot refresh
            //  also there should be a difference between 0 out of 0, and 0 out of 1 found
            //  the latter should offer a retry button
            FullScreenText(noResultsText)
        }
        else -> {
            // https://android-review.googlesource.com/c/platform/frameworks/support/+/2193512/19/compose/material/material/samples/src/main/java/androidx/compose/material/samples/PullRefreshSamples.kt#58
            // New rememberPullRefreshState is out, but seems to not work as nicely right now
            val swipeRefreshState = rememberSwipeRefreshState(false)
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { lazyPagingItems.refresh() },
                indicator = { state: SwipeRefreshState, refreshTrigger: Dp ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTrigger,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                }
            ) {
                LazyColumn(
                    modifier = modifier,
                    state = lazyListState,
                ) {

                    // Note that if we prepend/append items, it will cause scroll to top on config change.
//                item {
//                    Text(text = "blah")
//                }

                    itemsIndexed(lazyPagingItems) { index: Int, value: T? ->
                        itemContent(value)

                        // Is this inefficient? At least it preserves list state on configuration change.
                        if (index == lazyPagingItems.itemCount - 1) {
                            when (lazyPagingItems.loadState.append) {
                                is LoadState.Loading -> {
                                    FooterLoadingIndicator()
                                }
                                is LoadState.Error -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        RetryButton(onClick = { lazyPagingItems.refresh() })
                                    }
                                }
                                else -> {
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
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

