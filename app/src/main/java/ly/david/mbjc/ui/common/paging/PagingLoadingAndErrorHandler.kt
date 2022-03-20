package ly.david.mbjc.ui.common.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import ly.david.mbjc.ui.common.FullScreenLoadingIndicator

/**
 * Handles loading and errors for paging screens.
 */
@Composable
fun <T : Any> PagingLoadingAndErrorHandler(
    lazyPagingItems: LazyPagingItems<T>,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    noResultsText: String = "No results found.",
    content: @Composable () -> Unit
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
        lazyPagingItems.loadState.append.endOfPaginationReached &&
            lazyPagingItems.itemCount == 0 -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    text = noResultsText
                )
            }
        }
        else -> {
            if (lazyPagingItems.loadState.append is LoadState.Error) {
                LaunchedEffect(Unit) {
                    val retryClicked: SnackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                        message = "Failed to fetch more data.",
                        actionLabel = "Retry"
                    )
                    if (retryClicked == SnackbarResult.ActionPerformed) {
                        lazyPagingItems.retry()
                    }
                }
            }

            content()
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
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            text = "Couldn't fetch data from Music Brainz.\nCome back later or click below to try again."
        )
        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                lazyPagingItems.retry()
            }
        ) {
            Icon(Icons.Default.Refresh, "")
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = "Retry"
            )
        }
    }
}
