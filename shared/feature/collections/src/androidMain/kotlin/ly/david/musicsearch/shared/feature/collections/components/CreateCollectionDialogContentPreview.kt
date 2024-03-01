package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCreateCollectionDialog() {
    PreviewTheme {
        Surface {
            CreateCollectionDialogContent()
        }
    }
}
