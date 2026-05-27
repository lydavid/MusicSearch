package ly.david.musicsearch.ui.common.wikimedia

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.common.decodeUrl
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.relation.UrlListItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.readMore
import musicsearch.ui.common.generated.resources.wikipedia
import org.jetbrains.compose.resources.stringResource

fun LazyListScope.wikipediaSection(
    extract: WikipediaExtract,
    filterText: String = "",
    collapsed: Boolean = false,
    onCollapseExpand: () -> Unit = {},
) {
    val showExtract = extract.extract.isNotBlank() &&
        extract.extract.contains(filterText, ignoreCase = true)
    val showUrl = extract.wikipediaUrl.isNotBlank() &&
        extract.wikipediaUrl.contains(filterText, ignoreCase = true)
    if (showExtract || showUrl) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.wikipedia),
                collapsed = collapsed,
                onClick = onCollapseExpand,
            )
        }

        if (!collapsed) {
            item {
                val clipboard = LocalClipboard.current
                val coroutineScope = rememberCoroutineScope()
                val haptics = LocalHapticFeedback.current

                var expanded by remember { mutableStateOf(false) }

                if (showExtract) {
                    HighlightableText(
                        text = AnnotatedString(extract.extract),
                        highlightedText = filterText,
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {
                                    expanded = !expanded
                                },
                                onLongClick = {
                                    coroutineScope.launch {
                                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                        clipboard.setClipEntry(clipEntryWith(extract.extract))
                                    }
                                },
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .animateContentSize(),
                        maxLines = if (expanded) Int.MAX_VALUE else 4,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                UrlListItem(
                    relation = RelationListItemModel(
                        id = "wikipedia_section",
                        type = stringResource(Res.string.readMore),
                        linkedEntity = MusicBrainzEntityType.URL,
                        // TODO: eventually, migrate the stored url to be decoded
                        //  so that we can search with utf-8
                        //  at that point, decode the url before storing
                        name = extract.wikipediaUrl.decodeUrl(),
                        linkedEntityId = "wikipedia_section",
                    ),
                    filterText = filterText,
                )
            }
        }
    }
}
