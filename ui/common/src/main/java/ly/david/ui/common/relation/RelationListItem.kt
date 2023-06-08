package ly.david.ui.common.relation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ly.david.data.common.openUrl
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.ResourceIcon
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles
import ly.david.ui.common.theme.getSubTextColor

@Composable
fun RelationListItem(
    relation: RelationListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, id: String, title: String?) -> Unit = { _, _, _ -> },
) {

    val context = LocalContext.current

    ListItem(
        headlineContent = {
            Column {
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
        },
        modifier = modifier.clickable {
            val entity = relation.linkedResource
            if (entity == MusicBrainzResource.URL) {
                context.openUrl(relation.name)
            } else {
                onItemClick(entity, relation.linkedResourceId, relation.getNameWithDisambiguation())
            }
        }
    )
}

// region Previews
@DefaultPreviews
@Composable
private fun Artist() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_0",
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

@DefaultPreviews
@Composable
private fun Recording() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
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
// endregion
