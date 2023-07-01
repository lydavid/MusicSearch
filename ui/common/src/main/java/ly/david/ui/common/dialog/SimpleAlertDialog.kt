package ly.david.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ly.david.ui.common.R
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

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
        }
    )
}

@DefaultPreviews
@Composable
private fun Long() {
    PreviewTheme {
        Surface {
            SimpleAlertDialog(
                title = stringResource(id = R.string.delete_search_history_confirmation),
                confirmText = stringResource(id = R.string.yes),
                dismissText = stringResource(id = R.string.no)
            )
        }
    }
}
