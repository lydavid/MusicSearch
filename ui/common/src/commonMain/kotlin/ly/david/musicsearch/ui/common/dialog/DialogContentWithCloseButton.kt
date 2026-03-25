package ly.david.musicsearch.ui.common.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.icons.Clear
import ly.david.musicsearch.ui.common.icons.CustomIcons

@Composable
fun DialogWithCloseButton(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    BasicDialog(
        onDismiss = onDismiss,
    ) {
        DialogContentWithCloseButton(
            modifier = modifier,
            onDismiss = onDismiss,
            content = content,
        )
    }
}

@Composable
internal fun DialogContentWithCloseButton(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.padding(vertical = 16.dp)) {
        IconButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onDismiss,
        ) {
            Icon(
                imageVector = CustomIcons.Clear,
                contentDescription = "Close",
            )
        }
        content()
    }
}
