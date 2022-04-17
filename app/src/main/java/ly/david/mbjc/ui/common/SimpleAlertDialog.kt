package ly.david.mbjc.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
internal fun SimpleAlertDialog(
    title: String,
    confirmText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(title) },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(confirmText)
            }
        }
    )
}

// TODO: preview. currently can't due to api
