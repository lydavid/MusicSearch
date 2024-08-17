package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.core.theme.TextStyles
import ly.david.musicsearch.ui.core.theme.getSubTextColor

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
