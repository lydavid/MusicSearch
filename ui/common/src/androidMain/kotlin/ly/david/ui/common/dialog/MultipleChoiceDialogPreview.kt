package ly.david.ui.common.dialog

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
private fun Preview() {
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
