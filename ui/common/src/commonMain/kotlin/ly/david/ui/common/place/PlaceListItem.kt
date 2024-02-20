package ly.david.ui.common.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.common.ifNotNull
import ly.david.musicsearch.core.models.common.ifNotNullOrEmpty
import ly.david.musicsearch.core.models.getLifeSpanForDisplay
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.ui.common.listitem.DisambiguationText
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
                        style = TextStyles.getCardBodyTextStyle(),
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
        modifier = modifier.clickable { onPlaceClick(place) },
    )
}
