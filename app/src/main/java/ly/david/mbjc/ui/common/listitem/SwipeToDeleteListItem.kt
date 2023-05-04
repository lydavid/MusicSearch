package ly.david.mbjc.ui.common.listitem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import ly.david.mbjc.ui.common.SwipeToDeleteBackground

// TODO: if we fail to delete item, would be nice to show it again until of a red background
//  this may suggest we shouldn't use swipe to delete for remote items, as the ux will be strange no matter what we do
@Composable
internal fun SwipeToDeleteListItem(
    content: @Composable RowScope.() -> Unit,
    disable: Boolean = false,
    onDelete: () -> Unit
) {
    if (disable) {
        Row {
            content()
        }
    } else {
        SwipeToDeleteListItem(
            dismissContent = content,
            onDelete = onDelete
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteListItem(
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
