package ly.david.mbjc.ui.common.listitem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import ly.david.mbjc.ui.common.SwipeToDeleteBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SwipeToDeleteListItem(
    dismissContent: @Composable RowScope.() -> Unit,
    onDelete: () -> Unit
) {
    val dismissState = rememberDismissState()

    SwipeToDismiss(
        state = dismissState,
        background = {
            when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> {
                    SwipeToDeleteBackground(alignment = Alignment.CenterStart)
                }

                DismissDirection.EndToStart -> {
                    SwipeToDeleteBackground(alignment = Alignment.CenterEnd)
                }

                null -> Unit
            }
        },
        dismissContent = dismissContent
    )

    when {
        dismissState.isDismissed(DismissDirection.StartToEnd) ||
            dismissState.isDismissed(DismissDirection.EndToStart) -> {
            onDelete()
        }
    }
}
