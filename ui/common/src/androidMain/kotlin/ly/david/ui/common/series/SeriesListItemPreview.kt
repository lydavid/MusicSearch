package ly.david.ui.common.series

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

// region Previews
internal class SeriesPreviewParameterProvider : PreviewParameterProvider<SeriesListItemModel> {
    override val values = sequenceOf(
        SeriesListItemModel(
            id = "1",
            name = "series name",
        ),
        SeriesListItemModel(
            id = "1",
            name = "series name",
            disambiguation = "that one",
        ),
        SeriesListItemModel(
            id = "1",
            name = "series name",
            type = "Tour",
        ),
        SeriesListItemModel(
            id = "1",
            name = "series name",
            disambiguation = "that one",
            type = "Tour",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(SeriesPreviewParameterProvider::class) series: SeriesListItemModel,
) {
    PreviewTheme {
        Surface {
            SeriesListItem(series = series)
        }
    }
}
// endregion
