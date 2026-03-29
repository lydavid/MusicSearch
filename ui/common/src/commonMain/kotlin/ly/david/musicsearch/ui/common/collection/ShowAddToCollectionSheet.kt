package ly.david.musicsearch.ui.common.collection

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.EditACollectionFeedback
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import ly.david.musicsearch.ui.common.screen.showInBottomSheetForResult
import ly.david.musicsearch.ui.common.snackbar.FeedbackSnackbarVisuals

fun showAddToCollectionSheet(
    coroutineScope: CoroutineScope,
    overlayHost: OverlayHost,
    entityType: MusicBrainzEntityType,
    entityIds: List<String>,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
) {
    coroutineScope.launch {
        val result: SnackbarPopResultV2<Feedback<EditACollectionFeedback>> = overlayHost.showInBottomSheetForResult(
            AddToCollectionScreen(
                entityType = entityType,
                collectableIds = entityIds,
            ),
        )

        result.feedback?.let { feedback ->
            val message = feedback.getMessage()
            val snackbarResult = snackbarHostState.showSnackbar(
                visuals = FeedbackSnackbarVisuals(
                    message = message,
                    actionLabel = (feedback as? Feedback.Error)?.action?.name,
                    duration = when (feedback) {
                        is Feedback.Loading -> SnackbarDuration.Indefinite
                        is Feedback.Success,
                        is Feedback.Error,
                        is Feedback.Actionable,
                        -> SnackbarDuration.Short
                    },
                    withDismissAction = false,
                    feedback = feedback,
                ),
            )
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    onLoginClick()
                }

                SnackbarResult.Dismissed -> {
                    // Do nothing.
                }
            }
        }
    }
}
