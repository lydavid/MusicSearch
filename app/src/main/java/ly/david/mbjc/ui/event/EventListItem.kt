package ly.david.mbjc.ui.event

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.domain.EventListItemModel
import ly.david.data.getLifeSpanForDisplay
import ly.david.data.getNameWithDisambiguation
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

// TODO: pretty up

@Composable
internal fun EventListItem(
    event: EventListItemModel,
    onEventClick: EventListItemModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onEventClick(event) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(text = event.getNameWithDisambiguation())
            Text(text = event.type.orEmpty())
            Text(text = event.lifeSpan.getLifeSpanForDisplay())
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            EventListItem(
                event = EventListItemModel(
                    id = "1",
                    name = "event name",
                    disambiguation = "that one",
                    type = "Concert",
                )
            )
        }
    }
}
