package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewLabelDetailsUi() {
    PreviewWithSharedElementTransition {
        LabelDetailsTabUi(
            label = LabelDetailsModel(
                id = "f9ada3ae-3081-44df-8581-ca27a3462b68",
                name = "Sony BMG Music Entertainment",
                disambiguation = "Aug 5, 2004 - Oct 1, 2008",
                type = "Original Production",
                labelCode = 13989,
            ),
        )
    }
}
