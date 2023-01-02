package ly.david.mbjc.ui.common.fullscreen

import androidx.compose.runtime.Composable

/**
 * For displaying a [detailsScreen], showing a loading indicator when [scaffoldModel] is null,
 * handling errors when [showError], and delegating retry with [onRetryClick].
 */
@Composable
internal fun <T> DetailsWithErrorHandling(
    showError: Boolean,
    onRetryClick: () -> Unit,
    scaffoldModel: T?,
    detailsScreen: @Composable ((T) -> Unit)
) {
    when {
        showError -> {
            FullScreenErrorWithRetry(
                onClick = onRetryClick
            )
        }
        scaffoldModel == null -> {
            FullScreenLoadingIndicator()
        }
        else -> {
            detailsScreen(scaffoldModel)
        }
    }
}
