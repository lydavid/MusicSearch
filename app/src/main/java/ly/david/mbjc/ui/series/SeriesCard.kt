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
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.domain.EventUiModel
import ly.david.mbjc.data.domain.SeriesUiModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.transformThisIfNotNullOrEmpty
import ly.david.mbjc.ui.event.EventCard
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

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesPreview() {
    PreviewTheme {
        Surface {
            EventCard(
                eventUiModel = EventUiModel(
                    id = "1",
                    name = "series name",
                )
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesWithDisambiguationPreview() {
    PreviewTheme {
        Surface {
            EventCard(
                eventUiModel = EventUiModel(
                    id = "1",
                    name = "series name",
                    disambiguation = "that one",
                )
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SeriesWithTypePreview() {
    PreviewTheme {
        Surface {
            EventCard(
                eventUiModel = EventUiModel(
                    id = "1",
                    name = "series name",
                    type = "Tour",
                )
            )
        }
    }
}
