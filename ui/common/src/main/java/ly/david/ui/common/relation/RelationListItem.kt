package ly.david.ui.common.relation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.data.common.openUrl
import ly.david.musicsearch.data.core.getNameWithDisambiguation
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.ui.common.EntityIcon
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles
import ly.david.ui.core.theme.getSubTextColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RelationListItem(
    relation: RelationListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val context = LocalContext.current
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = "${relation.label}:",
                    style = TextStyles.getCardBodySubTextStyle(),
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    EntityIcon(
                        entity = relation.linkedEntity,
                        modifier = Modifier.padding(end = 8.dp),
                    )

                    Text(
                        text = relation.name,
                        style = TextStyles.getCardBodyTextStyle(),
                    )
                }

                val disambiguation = relation.disambiguation
                if (!disambiguation.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($disambiguation)",
                        style = TextStyles.getCardBodySubTextStyle(),
                        color = getSubTextColor(),
                    )
                }

                val attributes = relation.attributes
                if (!attributes.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($attributes)",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                val additionalInfo = relation.additionalInfo
                if (!additionalInfo.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = additionalInfo,
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    val entity = relation.linkedEntity
                    if (entity == MusicBrainzEntity.URL) {
                        context.openUrl(relation.name)
                    } else {
                        onItemClick(entity, relation.linkedEntityId, relation.getNameWithDisambiguation())
                    }
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(relation.name))
                },
            ),
    )
}

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewArtistRelationListItem() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_0",
                    linkedEntityId = "2",
                    linkedEntity = MusicBrainzEntity.ARTIST,
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
                ),
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewRecordingRelationListItem() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedEntityId = "2",
                    linkedEntity = MusicBrainzEntity.RECORDING,
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                ),
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewUrlRelationListItem() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedEntityId = "3",
                    linkedEntity = MusicBrainzEntity.URL,
                    label = "Stream for free",
                    name = "https://www.example.com",
                ),
            )
        }
    }
}
// endregion
