package ly.david.mbjc.ui.common

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import ly.david.mbjc.ui.theme.getAlertBackgroundColor

@Composable
fun SimpleAlertDialog(
    title: String,
    confirmText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(title) },
        backgroundColor = getAlertBackgroundColor(),
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
