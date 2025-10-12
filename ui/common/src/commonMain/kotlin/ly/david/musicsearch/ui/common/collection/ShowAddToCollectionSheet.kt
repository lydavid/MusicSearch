package ly.david.musicsearch.ui.common.collection

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.slack.circuit.overlay.OverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.AddToCollectionScreen
import ly.david.musicsearch.ui.common.screen.showInBottomSheet

fun showAddToCollectionSheet(
    coroutineScope: CoroutineScope,
    overlayHost: OverlayHost,
    entityType: MusicBrainzEntityType,
    entityIds: Set<String>,
    snackbarHostState: SnackbarHostState,
    onLoginClick: () -> Unit,
) {
    coroutineScope.launch {
        val result = overlayHost.showInBottomSheet(
            AddToCollectionScreen(
                entityType = entityType,
                collectableIds = entityIds,
            ),
        )
        result.message.ifNotNullOrEmpty {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = result.message,
                actionLabel = result.actionLabel,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
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
