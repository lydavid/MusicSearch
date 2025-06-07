package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.DisambiguationText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun RelationListItem(
    relation: RelationListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    ListItem(
        leadingContent = {
            ThumbnailImage(
                url = relation.imageUrl.orEmpty(),
                placeholderKey = relation.imageId.toString(),
                placeholderIcon = relation.linkedEntity.getIcon(),
                clipCircle = relation.linkedEntity == MusicBrainzEntity.ARTIST,
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

                relation.lifeSpan.getLifeSpanForDisplay().ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($it)",
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
                    coroutineScope.launch {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        clipboard.setClipEntry(clipEntryWith(relation.name))
                    }
                },
            ),
    )
}
