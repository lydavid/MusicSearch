package ly.david.mbjc.ui.recording

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.mbjc.data.persistence.recording.RecordingRelationRoomModel
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.DestinationIcon
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun RecordingRelationCard(
    relation: RecordingRelationRoomModel,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {

    ClickableListItem(onClick = {
        onItemClick(relation.destination, relation.linkedResourceId)
    }) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text(
                text = "${relation.label}:",
                style = TextStyles.getCardBodyTextStyle()
            )

            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                DestinationIcon(
                    destination = relation.destination,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Text(
                    text = relation.name,
                    style = TextStyles.getCardTitleTextStyle()
                )
            }

            val disambiguation = relation.disambiguation
            if (!disambiguation.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "($disambiguation)",
                    style = TextStyles.getCardBodyTextStyle(),
                    color = getSubTextColor()
                )
            }

            val attributes = relation.attributes
            if (!attributes.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "($attributes)",
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
internal fun RecordingRelationCardPreview() {
    PreviewTheme {
        Surface {
            RecordingRelationCard(
                relation = RecordingRelationRoomModel(
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
                    destination = Destination.LOOKUP_ARTIST,
                    recordingId = "1",
                    linkedResourceId = "2",
                    order = 0
                )
            )
        }
    }
}
