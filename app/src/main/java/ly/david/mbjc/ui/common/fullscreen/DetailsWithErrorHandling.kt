package ly.david.mbjc.ui.common.fullscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * For displaying a [detailsScreen], showing a loading indicator when [scaffoldModel] is null,
 * handling errors when [showError], and delegating retry with [onRetryClick].
 */
@Composable
internal fun <T> DetailsWithErrorHandling(
    modifier: Modifier = Modifier,
    showError: Boolean,
    onRetryClick: () -> Unit,
    scaffoldModel: T?,
    detailsScreen: @Composable ((T) -> Unit)
) {
    when {
        showError -> {
            FullScreenErrorWithRetry(
                modifier = modifier,
                onClick = onRetryClick
            )
        }
        scaffoldModel == null -> {
            FullScreenLoadingIndicator(modifier = modifier)
        }
        else -> {
            detailsScreen(scaffoldModel)
        }
    }
}
