package ly.david.musicsearch.ui.common.wikimedia

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun WikipediaSection(
    extract: WikipediaExtract,
    modifier: Modifier = Modifier,
    filterText: String = "",
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current

    Column(
        modifier = modifier,
    ) {
        if (extract.extract.isNotBlank() &&
            extract.extract.contains(filterText, ignoreCase = true)
        ) {
            var expanded by remember { mutableStateOf(false) }

            Text(
                text = extract.extract,
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
                color = MaterialTheme.colorScheme.onBackground,
                style = TextStyles.getCardBodyTextStyle(),
            )
        }

        if (extract.wikipediaUrl.isNotBlank() &&
            extract.wikipediaUrl.contains(filterText, ignoreCase = true)
        ) {
            val strings = LocalStrings.current

            UrlListItem(
                relation = RelationListItemModel(
                    id = "wikipedia_section",
                    label = strings.wikipedia,
                    linkedEntity = MusicBrainzEntity.URL,
                    name = extract.wikipediaUrl,
                    linkedEntityId = "wikipedia_section",
                ),
            )
        }
    }
}
