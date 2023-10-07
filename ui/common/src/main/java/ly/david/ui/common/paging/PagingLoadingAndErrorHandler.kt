package ly.david.ui.common.paging

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ly.david.data.core.Identifiable
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.button.RetryButton
import ly.david.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.ui.common.fullscreen.FullScreenText

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
@Composable
fun <T : Identifiable> PagingLoadingAndErrorHandler(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState? = null,
    customNoResultsText: String = "",
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
//    val refreshScope = rememberCoroutineScope()
//    var refreshing by remember { mutableStateOf(false) }

    // TODO: Issue with indicator remaining on screen without delay in refresh
    //  https://issuetracker.google.com/issues/248274004
    //  Fixed in: androidx.compose.material:material:1.4.0-alpha03
    //  Pulling down doesn't guarantee a refresh. Let's just wait for next release.
    //  Not available in M3: https://issuetracker.google.com/issues/261760718
//    fun refresh() = refreshScope.launch {
//        refreshing = true
//        lazyPagingItems.refresh()
//        delay(1000)
//        refreshing = false
//    }
//
//    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)

    val strings = LocalStrings.current
    val noResultsText = customNoResultsText.ifEmpty {
        strings.noResultsFound
    }

    val swipeRefreshState = rememberSwipeRefreshState(false)
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { lazyPagingItems.refresh() },
        modifier = modifier,
        indicator = { state: SwipeRefreshState, refreshTrigger: Dp ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                contentColor = MaterialTheme.colorScheme.primary
            )
        }
    ) {
        // This doesn't affect "loads" from db/source.
        when {
            lazyPagingItems.loadState.refresh is LoadState.Loading -> {
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
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { it.id },
                        contentType = { lazyPagingItems[it] },
                    ) { index ->
                        itemContent(lazyPagingItems[index])

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
