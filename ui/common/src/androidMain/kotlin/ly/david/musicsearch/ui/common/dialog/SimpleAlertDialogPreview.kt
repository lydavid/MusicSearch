package ly.david.musicsearch.ui.common.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSimpleAlertDialog() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SimpleAlertDialog(
                title = "Clear your search history?",
                confirmText = "Yes",
                dismissText = "No",
            )
        }
    }
}
