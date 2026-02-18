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
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.Header
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.ui.common.button.RetryButton
import ly.david.musicsearch.ui.common.fullscreen.FullScreenErrorWithRetry
import ly.david.musicsearch.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.noResultsFound
import org.jetbrains.compose.resources.stringResource

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
 * @param itemContent Composable UI for each [lazyPagingItems].
 *
 * Any [ListSeparator] will be displayed as a sticky header.
 */
@Suppress("ContentSlotReused") // crashes if we use moveableContentOf together with filtering
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
    val noResultsText = customNoResultsText.ifEmpty {
        stringResource(Res.string.noResultsFound)
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
                    for (index in 0 until lazyPagingItems.itemCount) {
                        when (val item = lazyPagingItems.peek(index)) {
                            is ListSeparator -> {
                                stickyHeader(
                                    key = item.id.takeIf { keyed },
                                ) {
                                    itemContent(this, lazyPagingItems[index])
                                }
                            }

                            else -> {
                                item(
                                    key = item?.id.takeIf { keyed },
                                ) {
                                    itemContent(this, lazyPagingItems[index])
                                }

                                if (index == lazyPagingItems.itemCount - 1) {
                                    item {
                                        AppendFooter(lazyPagingItems)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : Identifiable> LazyItemScope.AppendFooter(
    lazyPagingItems: LazyPagingItems<T>,
) {
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
                RetryButton(onClick = { lazyPagingItems.retry() })
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
