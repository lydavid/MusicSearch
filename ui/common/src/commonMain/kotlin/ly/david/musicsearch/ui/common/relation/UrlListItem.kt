package ly.david.musicsearch.ui.common.relation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Link
import ly.david.musicsearch.ui.common.icons.Wikidata
import ly.david.musicsearch.ui.common.icons.Wikipedia
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.text.fontWeight
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.readMore
import musicsearch.ui.common.generated.resources.wikipedia
import org.jetbrains.compose.resources.stringResource

@Composable
fun UrlListItem(
    relation: RelationListItemModel,
    filterText: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val uriHandler = LocalUriHandler.current

    ListItem(
        headlineContent = {
            Column {
                HighlightableText(
                    text = "${relation.type}:",
                    highlightedText = filterText,
                    style = TextStyles.getCardBodySubTextStyle(),
                )

                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val imageVectorAndTint = when (relation.type) {
                        stringResource(Res.string.readMore),
                        stringResource(Res.string.wikipedia),
                        -> {
                            CustomIcons.Wikipedia to LocalContentColor.current
                        }

                        "Wikidata" -> {
                            CustomIcons.Wikidata to Color.Unspecified
                        }

                        else -> {
                            CustomIcons.Link to LocalContentColor.current
                        }
                    }
                    Icon(
                        imageVector = imageVectorAndTint.first,
                        contentDescription = null,
                        tint = imageVectorAndTint.second,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp),
                    )

                    HighlightableText(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = relation.fontWeight)) {
                                append(relation.name)
                            }
                        },
                        highlightedText = filterText,
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
                    coroutineScope.launch {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        clipboard.setClipEntry(clipEntryWith(relation.name))
                    }
                },
            ),
    )
}
