package ly.david.mbjc.ui.common.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun SimpleAlertDialog(
    title: String,
    confirmText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                style = TextStyles.getCardTitleTextStyle(),
            )
        },
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

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            SimpleAlertDialog(title = "Title", confirmText = "OK") {}
        }
    }
}

@DefaultPreviews
@Composable
private fun Long() {
    PreviewTheme {
        Surface {
            SimpleAlertDialog(title = "Title text that is long enough to wrap", confirmText = "OK") {}
        }
    }
}
