package ly.david.ui.common.event

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
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.ui.common.listitem.DisambiguationText
import ly.david.ui.core.theme.TextStyles

@Composable
fun EventListItem(
    event: EventListItemModel,
    modifier: Modifier = Modifier,
    onEventClick: EventListItemModel.() -> Unit = {},
) {
    ListItem(
        headlineContent = {
            Column {
                event.run {
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
        modifier = modifier.clickable {
            onEventClick(event)
        },
    )
}
