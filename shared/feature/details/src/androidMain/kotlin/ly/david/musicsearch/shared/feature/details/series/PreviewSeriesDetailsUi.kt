package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.series.SeriesScaffoldModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewSeriesDetailsUi() {
    PreviewTheme {
        Surface {
            SeriesDetailsUi(
                series = SeriesScaffoldModel(
                    id = "2bb59d7e-88f9-455d-888e-802b5f688dac",
                    name = "Pulitzer Prize for Music",
                ),
            )
        }
    }
}
