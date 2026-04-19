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
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.image.ThumbnailImage
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
fun RelationListItem(
    relation: RelationListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    ListItem(
        leadingContent = {
            ThumbnailImage(
                imageMetadata = relation.imageMetadata,
                placeholderIcon = relation.linkedEntity.getIcon(),
                clipCircle = relation.linkedEntity == MusicBrainzEntityType.ARTIST,
            )
        },
        headlineContent = {
            Column {
                relation.type.ifNotEmpty { type ->
                    Text(
                        text = "$type:",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                HighlightableText(
                    text = relation.getAnnotatedName(),
                    highlightedText = filterText,
                    style = TextStyles.getCardBodyTextStyle(),
                )
            }
        },
        supportingContent = {
            Column {
                relation.attributes.ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($it)",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }

                relation.lifeSpan.getLifeSpanForDisplay().ifNotNullOrEmpty {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "($it)",
                        style = TextStyles.getCardBodySubTextStyle(),
                    )
                }
            }
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    val entity = relation.linkedEntity
                    val entityId = relation.linkedEntityId
                    onItemClick(
                        entity,
                        entityId,
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
