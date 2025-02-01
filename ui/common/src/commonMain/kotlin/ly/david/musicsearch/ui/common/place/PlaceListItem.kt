package ly.david.musicsearch.ui.common.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

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
                        fontWeight = place.fontWeight,
                    )

                    DisambiguationText(
                        disambiguation = disambiguation,
                        fontWeight = place.fontWeight,
                    )

                    type.ifNotNullOrEmpty {
                        Text(
                            text = it,
                            modifier = Modifier.padding(top = 4.dp),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = place.fontWeight,
                        )
                    }

                    address.ifNotNullOrEmpty {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it,
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = place.fontWeight,
                        )
                    }

                    lifeSpan.ifNotNull {
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = it.getLifeSpanForDisplay(),
                            style = TextStyles.getCardBodySubTextStyle(),
                            fontWeight = place.fontWeight,
                        )
                    }
                }
            }
        },
        modifier = modifier.clickable { onPlaceClick(place) },
    )
}
