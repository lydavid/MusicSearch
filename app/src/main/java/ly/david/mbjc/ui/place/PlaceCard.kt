package ly.david.mbjc.ui.place

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.domain.PlaceUiModel
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun PlaceCard(
    place: PlaceUiModel,
    onPlaceClick: PlaceUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onPlaceClick(place) },
    ) {
        // TODO:
        Column {
            Text(text = place.getNameWithDisambiguation())
            Text(text = place.address)
            Text(text = place.type ?: "n")
            Text(text = place.lifeSpan.getLifeSpanForDisplay())
            Text(text = place.coordinates?.toString() ?: "n")
        }
    }
}

// Cannot be private.
internal class PlaceCardPreviewParameterProvider : PreviewParameterProvider<PlaceUiModel> {
    override val values = sequenceOf(
        PlaceUiModel(
            id = "2",
            name = "Place Name",
            address = "123 Fake St"
        ),
        PlaceUiModel(
            id = "ed121457-87f6-4df9-a24b-d3f1bab1fdad",
            name = "Sony Music Studios",
            disambiguation = "NYC, closed 2007",
            type = "Studio",
            address = "460 W. 54th St., at 10th Avenue, Manhatten, NY",
            lifeSpan = LifeSpan(begin = "1993", end = "2007-08", ended = true)
        ),
        PlaceUiModel(
            id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            name = "日本武道館",
            type = "Indoor arena",
            address = "〒102-8321 東京都千代田区北の丸公園2-3",
            lifeSpan = LifeSpan(begin = "1964-10-03")
        ),
    )
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlaceCardPreview(
    @PreviewParameter(PlaceCardPreviewParameterProvider::class) place: PlaceUiModel
) {
    PreviewTheme {
        Surface {
            PlaceCard(place)
        }
    }
}
