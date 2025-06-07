package ly.david.musicsearch.ui.common.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewMultipleChoiceDialog() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MultipleChoiceDialog(
                title = "Theme",
                choices = listOf(
                    "Light",
                    "Dark",
                    "System",
                ),
                selectedChoiceIndex = 0,
                onSelectChoiceIndex = {},
            )
        }
    }
}
