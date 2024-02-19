package ly.david.ui.common.fullscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * For displaying a [detailsScreen], showing a loading indicator when [scaffoldModel] is null,
 * handling errors when [showError], and delegating retry with [onRetryClick].
 */
@Composable
fun <T> DetailsWithErrorHandling(
    showError: Boolean,
    onRetryClick: () -> Unit,
    scaffoldModel: T?,
    modifier: Modifier = Modifier,
    detailsScreen: @Composable ((T) -> Unit),
) {
    when {
        showError -> {
            FullScreenErrorWithRetry(
                modifier = modifier,
                onClick = onRetryClick,
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
