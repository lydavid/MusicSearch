package ly.david.musicsearch.ui.common.listitem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
    modifier: Modifier = Modifier,
    disable: Boolean = false,
    onDelete: () -> Unit = {},
    content: @Composable RowScope.() -> Unit = {},
) {
    if (disable) {
        Row(modifier = modifier) {
            content()
        }
    } else {
        SwipeToDeleteListItem(
            onDelete = onDelete,
            modifier = modifier,
            dismissContent = content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteListItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    dismissContent: @Composable RowScope.() -> Unit = {},
) {
    val dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd ||
        dismissState.currentValue == SwipeToDismissBoxValue.EndToStart
    ) {
        onDelete()
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    SwipeToDeleteBackground(alignment = Alignment.CenterStart)
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    SwipeToDeleteBackground(alignment = Alignment.CenterEnd)
                }

                else -> {}
            }
        },
        content = dismissContent,
        modifier = modifier,
    )
}

@Composable
private fun SwipeToDeleteBackground(
    alignment: Alignment,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red),
    ) {
        Image(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment),
            colorFilter = ColorFilter.tint(Color.White),
        )
    }
}
