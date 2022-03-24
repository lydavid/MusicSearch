package ly.david.mbjc.ui.common.paging

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import kotlinx.coroutines.flow.flowOf
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.fullscreen.FullScreenLoadingIndicator
import ly.david.mbjc.ui.common.fullscreen.FullScreenText
import ly.david.mbjc.ui.theme.MusicBrainzJetpackComposeTheme

/**
 * Handles loading and errors for paging screens.
 *
 * @param modifier For lazy column containing [content].
 */
@Composable
fun <T : Any> PagingLoadingAndErrorHandler(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    lazyListState: LazyListState = rememberLazyListState(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    noResultsText: String = stringResource(id = R.string.no_results_found),
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
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
                scaffoldState.snackbarHostState.showSnackbar(displayMessage)
            }

            FullScreenErrorWithRetry(lazyPagingItems = lazyPagingItems)
        }
        lazyPagingItems.loadState.append.endOfPaginationReached && lazyPagingItems.itemCount == 0 -> {
            FullScreenText(noResultsText)
        }
        else -> {
            LazyColumn(
                modifier = modifier,
                state = lazyListState,
            ) {
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
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RetryButton(lazyPagingItems = lazyPagingItems)
                                }
                            }
                            else -> {
                                Spacer(modifier = Modifier.fillMaxWidth().padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun <T : Any> FullScreenErrorWithRetry(
    lazyPagingItems: LazyPagingItems<T>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            text = "Couldn't fetch data from Music Brainz.\nCome back later or click below to try again."
        )
        RetryButton(lazyPagingItems = lazyPagingItems)
    }
}

@Composable
private fun <T : Any> RetryButton(lazyPagingItems: LazyPagingItems<T>) {
    Button(
        onClick = { lazyPagingItems.retry() }
    ) {
        Icon(Icons.Default.Refresh, "")
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = "Retry"
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun FullScreenErrorWithRetryPreview() {
    MusicBrainzJetpackComposeTheme {
        Surface {
            FullScreenErrorWithRetry(flowOf(PagingData.from(listOf())).collectAsLazyPagingItems())
        }
    }
}
