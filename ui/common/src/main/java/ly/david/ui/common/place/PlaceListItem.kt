package ly.david.ui.common.place

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
import ly.david.data.core.common.ifNotNull
import ly.david.data.core.common.ifNotNullOrEmpty
import ly.david.data.core.getLifeSpanForDisplay
import ly.david.musicsearch.domain.common.LifeSpanUiModel
import ly.david.musicsearch.domain.listitem.PlaceListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
fun PlaceListItem(
    place: PlaceListItemModel,
    modifier: Modifier = Modifier,
    onPlaceClick: PlaceListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                place.run {
                    Text(
                        text = name,
                        style = TextStyles.getCardBodyTextStyle()
                    )

                    DisambiguationText(disambiguation = disambiguation)

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                        )
                    }

                    address.ifNotNullOrEmpty {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
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
        },
        modifier = modifier.clickable { onPlaceClick(place) }
    )
}

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
            lifeSpan = LifeSpanUiModel(begin = "1993", end = "2007-08", ended = true)
        ),
        PlaceListItemModel(
            id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            name = "日本武道館",
            type = "Indoor arena",
            address = "〒102-8321 東京都千代田区北の丸公園2-3",
            lifeSpan = LifeSpanUiModel(begin = "1964-10-03")
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(PlacePreviewParameterProvider::class) place: PlaceListItemModel,
) {
    PreviewTheme {
        Surface {
            PlaceListItem(place)
        }
    }
}
