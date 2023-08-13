package ly.david.ui.common.listitem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp

// TODO: if we fail to delete item, would be nice to show it again instead of a red background
//  this may suggest we shouldn't use swipe to delete for remote items, as the ux will be strange no matter what we do
@Composable
fun SwipeToDeleteListItem(
    content: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    disable: Boolean = false,
    onDelete: () -> Unit,
) {
    if (disable) {
        Row(modifier = modifier) {
            content()
        }
    } else {
        SwipeToDeleteListItem(
            dismissContent = content,
            onDelete = onDelete,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteListItem(
    dismissContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
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
        dismissContent = dismissContent,
        modifier = modifier
    )

    when {
        dismissState.isDismissed(DismissDirection.StartToEnd) ||
            dismissState.isDismissed(DismissDirection.EndToStart) -> {
            onDelete()
        }
    }
}

@Composable
private fun SwipeToDeleteBackground(
    alignment: Alignment,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Image(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}
