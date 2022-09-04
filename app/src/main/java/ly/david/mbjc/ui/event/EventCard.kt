package ly.david.mbjc.ui.event

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
import ly.david.mbjc.data.getLifeSpanForDisplay
import ly.david.mbjc.data.getNameWithDisambiguation
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun EventCard(
    eventUiModel: EventUiModel,
    onEventClick: EventUiModel.() -> Unit = {}
) {
    ClickableListItem(
        onClick = { onEventClick(eventUiModel) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        ) {
            Text(text = eventUiModel.getNameWithDisambiguation())
            Text(text = eventUiModel.type.orEmpty())
            Text(text = eventUiModel.lifeSpan.getLifeSpanForDisplay())
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            EventCard(
                eventUiModel = EventUiModel(
                    id = "1",
                    name = "event name",
                    disambiguation = "that one",
                    type = "Concert",
                )
            )
        }
    }
}

//@Preview
//@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
//@Composable
//private fun LifeSpanPreview() {
//    PreviewTheme {
//        Surface {
//            EventCard(
//                eventUiModel = EventUiModel(
//                    id = "1",
//                    name = "work name",
//                    disambiguation = "that one",
//                    type = "Song",
//                    lifeSpan = LifeSpan(
//
//                    )
//                )
//            )
//        }
//    }
//}
