package ly.david.ui.common.fullscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * For displaying a [detailsScreen], showing a loading indicator when [scaffoldModel] is null,
 * handling errors when [showError], and delegating retry with [onRefresh].
 * Supports pull to refresh, delegating to [onRefresh].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DetailsWithErrorHandling(
    showLoading: Boolean = false,
    showError: Boolean = false,
    onRefresh: () -> Unit,
    scaffoldModel: T?,
    modifier: Modifier = Modifier,
    detailsScreen: @Composable ((T) -> Unit),
) {
    when {
        showError -> {
            FullScreenErrorWithRetry(
                modifier = modifier,
                onClick = onRefresh,
            )
        }

        scaffoldModel == null -> {
            FullScreenLoadingIndicator(modifier = modifier)
        }

        else -> {
            val refreshState = rememberPullRefreshState(
                refreshing = showLoading,
                onRefresh = { onRefresh() },
            )
            Box(
                modifier = modifier.pullRefresh(refreshState),
            ) {
                detailsScreen(scaffoldModel)

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
