package ly.david.mbjc.ui.event

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
import ly.david.data.LifeSpan
import ly.david.data.common.ifNotNull
import ly.david.data.common.ifNotNullOrEmpty
import ly.david.data.domain.EventListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.DisambiguationText
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

@Composable
internal fun EventListItem(
    event: EventListItemModel,
    modifier: Modifier = Modifier,
    onEventClick: EventListItemModel.() -> Unit = {}
) {
    ListItem(
        headlineText = {
            Column {
                event.run {

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

@ExcludeFromJacocoGeneratedReport
internal class EventPreviewParameterProvider : PreviewParameterProvider<EventListItemModel> {
    override val values = sequenceOf(
        EventListItemModel(
            id = "e1",
            name = "event name",
            disambiguation = "that one",
            type = "Concert",
        ),
        EventListItemModel(
            id = "05174e82-7716-444e-86a0-d0d1e1474662",
            name = "1998-01-22: Sony Music Studios, New York City, NY, USA",
            disambiguation = "“Where It’s At: The Rolling Stone State Of The Union”, " +
                "a Rolling Stone Magazine 30th anniversary special",
            type = null,
            lifeSpan = LifeSpan(
                begin = "1998-01-22",
                end = "1998-01-22",
                ended = true
            )
        ),
    )
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(EventPreviewParameterProvider::class) event: EventListItemModel
) {
    PreviewTheme {
        Surface {
            EventListItem(event)
        }
    }
}
