package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.label.LabelScaffoldModel
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewLabelDetailsUi() {
    PreviewTheme {
        Surface {
            LabelDetailsUi(
                label = LabelScaffoldModel(
                    id = "f9ada3ae-3081-44df-8581-ca27a3462b68",
                    name = "Sony BMG Music Entertainment",
                    disambiguation = "Aug 5, 2004 - Oct 1, 2008",
                    type = "Original Production",
                    labelCode = 13989,
                ),
            )
        }
    }
}
