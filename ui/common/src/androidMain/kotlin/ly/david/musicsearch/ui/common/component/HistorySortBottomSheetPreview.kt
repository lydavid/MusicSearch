package ly.david.musicsearch.ui.common.component

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewMultipleChoiceBottomSheetContent() {
    PreviewTheme {
        Surface {
            MultipleChoiceBottomSheetContent(
                options = listOf(
                    "Alphabetically",
                    "Reverse alphabetically",
                    "Recently visited",
                    "Least recently visited",
                    "Most visited",
                    "Least visited",
                ),
                selectedOptionIndex = 2,
            )
        }
    }
}
