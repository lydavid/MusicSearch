package ly.david.musicsearch.ui.common.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.error.Feedback

@Composable
fun FeedbackSnackbarHost(snackbarHostState: SnackbarHostState) {
    SnackbarHost(hostState = snackbarHostState) { snackbarData ->
        SwipeToDismissBox(
            state = rememberSwipeToDismissBoxState(),
            backgroundContent = {},
            onDismiss = { snackbarData.dismiss() },
            content = {
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = when ((snackbarData.visuals as? FeedbackSnackbarVisuals)?.feedback) {
                        is Feedback.Error -> MaterialTheme.colorScheme.error
                        is Feedback.Success -> MaterialTheme.colorScheme.primary
                        is Feedback.Loading,
                        is Feedback.Actionable,
                        null,
                        -> SnackbarDefaults.color
                    },
                )
            },
        )
    }
}
