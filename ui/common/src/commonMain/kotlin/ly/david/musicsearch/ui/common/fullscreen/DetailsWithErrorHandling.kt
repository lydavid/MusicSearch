package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.error.HandledException

/**
 * For displaying a [detailsScreen], showing a loading indicator when [detailsModel] is null,
 * handling errors when [handledException] is not null, and delegating retry with [onRefresh].
 * Supports pull to refresh, delegating to [onRefresh].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DetailsWithErrorHandling(
    detailsModel: T?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    handledException: HandledException? = null,
    detailsScreen: @Composable ((T) -> Unit),
) {
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = modifier,
        state = refreshState,
        isRefreshing = isLoading,
        onRefresh = onRefresh,
    ) {
        when {
            handledException != null -> {
                FullScreenErrorWithRetry(
                    handledException = handledException,
                    onClick = onRefresh,
                )
            }

            detailsModel == null -> {
                FullScreenLoadingIndicator()
            }

            else -> {
                detailsScreen(detailsModel)
            }
        }
    }
}
