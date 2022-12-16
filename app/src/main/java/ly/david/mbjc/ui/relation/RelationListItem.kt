package ly.david.mbjc.ui.relation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ly.david.data.common.openUrl
import ly.david.data.domain.RelationListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.navigation.Destination
import ly.david.data.navigation.toDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles
import ly.david.mbjc.ui.theme.getSubTextColor

@Composable
internal fun RelationListItem(
    relation: RelationListItemModel,
    onItemClick: (destination: Destination, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    val context = LocalContext.current

    ClickableListItem(onClick = {
        val destination = relation.linkedResource.toDestination()
        if (destination == Destination.LOOKUP_URL) {
            context.openUrl(relation.name)
        } else {
            onItemClick(destination, relation.linkedResourceId, relation.getNameWithDisambiguation())
        }
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
            RelationListItem(
                relation = RelationListItemModel(
                    linkedResourceId = "2",
                    linkedResource = MusicBrainzResource.ARTIST,
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
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
            RelationListItem(
                relation = RelationListItemModel(
                    linkedResourceId = "2",
                    linkedResource = MusicBrainzResource.RECORDING,
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                )
            )
        }
    }
}
