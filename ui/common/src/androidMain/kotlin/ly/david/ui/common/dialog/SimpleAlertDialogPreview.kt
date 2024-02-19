package ly.david.ui.common.dialog

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
