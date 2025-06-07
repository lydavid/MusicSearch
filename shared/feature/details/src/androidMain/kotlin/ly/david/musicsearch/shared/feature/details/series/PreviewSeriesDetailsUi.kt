package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewSeriesDetailsUi() {
    PreviewWithSharedElementTransition {
        SeriesDetailsTabUi(
            series = SeriesDetailsModel(
                id = "2bb59d7e-88f9-455d-888e-802b5f688dac",
                name = "Pulitzer Prize for Music",
            ),
        )
    }
}
