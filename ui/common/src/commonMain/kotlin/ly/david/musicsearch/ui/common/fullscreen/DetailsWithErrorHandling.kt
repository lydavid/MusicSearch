package ly.david.musicsearch.ui.common.fullscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * For displaying a [detailsScreen], showing a loading indicator when [detailsModel] is null,
 * handling errors when [showError], and delegating retry with [onRefresh].
 * Supports pull to refresh, delegating to [onRefresh].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DetailsWithErrorHandling(
    detailsModel: T?,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    showLoading: Boolean = false,
    showError: Boolean = false,
    detailsScreen: @Composable ((T) -> Unit),
) {
    val refreshState = rememberPullRefreshState(
        refreshing = showLoading,
        onRefresh = { onRefresh() },
    )
    Box(
        modifier = modifier.pullRefresh(refreshState),
    ) {
        when {
            showError -> {
                FullScreenErrorWithRetry(
                    onClick = onRefresh,
                )
            }

            detailsModel == null -> {
                FullScreenLoadingIndicator()
            }

            else -> {
                detailsScreen(detailsModel)

                PullRefreshIndicator(
                    refreshing = showLoading,
                    state = refreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                )
            }
        }
    }
}
