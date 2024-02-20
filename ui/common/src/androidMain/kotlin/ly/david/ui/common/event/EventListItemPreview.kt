package ly.david.ui.common.event

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
            lifeSpan = LifeSpanUiModel(
                begin = "1998-01-22",
                end = "1998-01-22",
                ended = true,
            ),
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(EventPreviewParameterProvider::class) event: EventListItemModel,
) {
    PreviewTheme {
        Surface {
            EventListItem(event)
        }
    }
}
