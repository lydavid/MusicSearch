package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.image.ThumbnailImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RelationListItem(
    relation: RelationListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

    ListItem(
        leadingContent = {
            val clipModifier = if (relation.linkedEntity == MusicBrainzEntity.ARTIST) {
                Modifier.clip(CircleShape)
            } else {
                Modifier
            }
            ThumbnailImage(
                url = relation.imageUrl.orEmpty(),
                placeholderKey = relation.imageId.toString(),
                placeholderIcon = relation.linkedEntity.getIcon(),
                modifier = clipModifier,
            )
        },
        headlineContent = {
            Column {
                Text(
                    text = "${relation.label}:",
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = relation.fontWeight,
                )

                Text(
                    text = relation.name,
                    style = TextStyles.getCardBodyTextStyle(),
                    fontWeight = relation.fontWeight,
                )
            }
        },
        supportingContent = {
            Column {
                DisambiguationText(
                    disambiguation = relation.disambiguation,
                    fontWeight = relation.fontWeight,
                )

                relation.attributes.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($it)",
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = relation.fontWeight,
                    )
                }

                relation.additionalInfo.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = it,
                        style = TextStyles.getCardBodySubTextStyle(),
                        fontWeight = relation.fontWeight,
                    )
                }
            }
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    val entity = relation.linkedEntity
                    onItemClick(
                        entity,
                        relation.linkedEntityId,
                        relation.getNameWithDisambiguation(),
                    )
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(relation.name))
                },
            ),
    )
}
