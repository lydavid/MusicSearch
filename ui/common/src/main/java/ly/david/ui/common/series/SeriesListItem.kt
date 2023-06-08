package ly.david.ui.common.series

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.listitem.SeriesListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

@Composable
fun SeriesListItem(
    series: SeriesListItemModel,
    modifier: Modifier = Modifier,
    onSeriesClick: SeriesListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineContent = {
            Column {
                series.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardTitleTextStyle(),
                    )

                    DisambiguationText(disambiguation = disambiguation)

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodyTextStyle(),
                        )
                    }
                }
            }
        },
        modifier = modifier.clickable { onSeriesClick(series) }
    )
}

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
    @PreviewParameter(SeriesPreviewParameterProvider::class) series: SeriesListItemModel
) {
    PreviewTheme {
        Surface {
            SeriesListItem(series = series)
        }
    }
}
// endregion
