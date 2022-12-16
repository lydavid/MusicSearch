package ly.david.mbjc.ui.place

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun PlaceListItem(
    place: PlaceListItemModel,
    onPlaceClick: PlaceListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onPlaceClick(place) },
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            place.run {
                Text(
                    text = name,
                    style = TextStyles.getCardTitleTextStyle()
                )

                DisambiguationText(disambiguation = disambiguation)

                type.ifNotNullOrEmpty {
                    Text(
                        text = it,
                        modifier = Modifier.padding(top = 4.dp),
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                address.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                // TODO: too much information on list item?
//                area.ifNotNull {
//                    Text(
//                        modifier = Modifier.padding(top = 4.dp),
//                        text = it.name,
//                        style = TextStyles.getCardBodyTextStyle(),
//                    )
//                }

                lifeSpan.ifNotNull {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it.getLifeSpanForDisplay(),
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        }
    }
}

// Cannot be private.
@ExcludeFromJacocoGeneratedReport
internal class PlacePreviewParameterProvider : PreviewParameterProvider<PlaceListItemModel> {
    override val values = sequenceOf(
        PlaceListItemModel(
            id = "2",
            name = "Place Name",
            address = "123 Fake St"
        ),
        PlaceListItemModel(
            id = "ed121457-87f6-4df9-a24b-d3f1bab1fdad",
            name = "Sony Music Studios",
            disambiguation = "NYC, closed 2007",
            type = "Studio",
            address = "460 W. 54th St., at 10th Avenue, Manhatten, NY",
            lifeSpan = LifeSpan(begin = "1993", end = "2007-08", ended = true)
        ),
        PlaceListItemModel(
            id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            name = "日本武道館",
            type = "Indoor arena",
            address = "〒102-8321 東京都千代田区北の丸公園2-3",
            lifeSpan = LifeSpan(begin = "1964-10-03")
        ),
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(PlacePreviewParameterProvider::class) place: PlaceListItemModel
) {
    PreviewTheme {
        Surface {
            PlaceListItem(place)
        }
    }
}
