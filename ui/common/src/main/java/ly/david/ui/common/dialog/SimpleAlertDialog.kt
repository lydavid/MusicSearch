package ly.david.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun SimpleAlertDialog(
    title: String,
    confirmText: String,
    dismissText: String,
    onDismiss: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                style = TextStyles.getHeaderTextStyle(),
            )
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmClick()
                onDismiss()
            }) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(dismissText)
            }
        },
    )
}

@DefaultPreviews
@Composable
internal fun PreviewSimpleAlertDialog() {
    PreviewTheme {
        Surface {
            SimpleAlertDialog(
                title = "Clear your search history?",
                confirmText = "Yes",
                dismissText = "No",
            )
        }
    }
}
