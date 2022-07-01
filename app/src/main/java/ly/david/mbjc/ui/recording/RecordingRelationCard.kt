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
import ly.david.mbjc.data.domain.RecordingRelationUiModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.ClickableListItem
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.navigation.toDestination
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

// TODO: since this doesn't reference recordingId, can be generalized for all relation cards
@Composable
internal fun RecordingRelationCard(
    relation: RecordingRelationUiModel,
    onItemClick: (destination: Destination, id: String) -> Unit = { _, _ -> },
) {

    ClickableListItem(onClick = {
        onItemClick(relation.linkedResource.toDestination(), relation.linkedResourceId)
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

                ResourceIcon(
                    resource = relation.linkedResource,
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

            val additionalInfo = relation.additionalInfo
            if (!additionalInfo.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = additionalInfo,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Artist() {
    PreviewTheme {
        Surface {
            RecordingRelationCard(
                relation = RecordingRelationUiModel(
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
                    linkedResource = MusicBrainzResource.ARTIST,
                    recordingId = "1",
                    linkedResourceId = "2",
                    order = 0
                )
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Recording() {
    PreviewTheme {
        Surface {
            RecordingRelationCard(
                relation = RecordingRelationUiModel(
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                    linkedResource = MusicBrainzResource.RECORDING,
                    recordingId = "1",
                    linkedResourceId = "2",
                    order = 0
                )
            )
        }
    }
}
