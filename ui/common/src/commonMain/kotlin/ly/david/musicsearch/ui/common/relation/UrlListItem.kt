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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.core.theme.TextStyles

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UrlListItem(
    relation: RelationListItemModel,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    ListItem(
        headlineContent = {
            Column {
                Text(
                    text = "${relation.label}:",
                    style = TextStyles.getCardBodySubTextStyle(),
                    fontWeight = relation.fontWeight,
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
                        fontWeight = relation.fontWeight,
                    )
                }
            }
        },
        modifier = modifier
            .combinedClickable(
                onClick = {
                    uriHandler.openUri(relation.name)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    clipboardManager.setText(AnnotatedString(relation.name))
                },
            ),
    )
}
