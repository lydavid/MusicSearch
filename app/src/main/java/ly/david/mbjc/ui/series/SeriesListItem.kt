package ly.david.mbjc.ui.series

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.common.transformThisIfNotNullOrEmpty
import ly.david.data.domain.SeriesListItemModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun SeriesListItem(
    series: SeriesListItemModel,
    onSeriesClick: SeriesListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onSeriesClick(series) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(
                text = series.name,
                style = TextStyles.getCardTitleTextStyle(),
            )

            if (!series.disambiguation.isNullOrEmpty()) {
                Text(
                    text = series.disambiguation.transformThisIfNotNullOrEmpty { "($it)" },
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor(),
                )
            }

            val type = series.type
            if (!type.isNullOrEmpty()) {
                Text(
                    text = type,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }
    }
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
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
