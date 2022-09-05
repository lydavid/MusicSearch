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
import ly.david.mbjc.data.domain.SeriesUiModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun SeriesCard(
    series: SeriesUiModel,
    onSeriesClick: SeriesUiModel.() -> Unit = {}
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

            if (!series.type.isNullOrEmpty()) {
                Text(
                    text = series.type,
                    style = TextStyles.getCardBodySubTextStyle(),
                )
            }
        }
    }
}

// region Previews
internal class SeriesPreviewParameterProvider : PreviewParameterProvider<SeriesUiModel> {
    override val values = sequenceOf(
        SeriesUiModel(
            id = "1",
            name = "series name",
        ),
        SeriesUiModel(
            id = "1",
            name = "series name",
            disambiguation = "that one",
        ),
        SeriesUiModel(
            id = "1",
            name = "series name",
            type = "Tour",
        ),
        SeriesUiModel(
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
    @PreviewParameter(SeriesPreviewParameterProvider::class) series: SeriesUiModel
) {
    PreviewTheme {
        Surface {
            SeriesCard(series = series)
        }
    }
}
// endregion
