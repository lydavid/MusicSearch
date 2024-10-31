package ly.david.musicsearch.ui.common.wikimedia

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.relation.RelationListItem
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
fun WikipediaSection(
    extract: WikipediaExtract,
    modifier: Modifier = Modifier,
    filterText: String = "",
) {
    Column(
        modifier = modifier,
    ) {
        if (extract.extract.isNotBlank() &&
            extract.extract.contains(filterText, ignoreCase = true)
        ) {
            var expanded by remember { mutableStateOf(false) }

            // TODO: consider expand/collapse icon button, then make text selectable
            Text(
                text = extract.extract,
                modifier = Modifier
                    .clickable { expanded = !expanded }
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
            val uriHandler = LocalUriHandler.current
            val strings = LocalStrings.current

            RelationListItem(
                relation = RelationListItemModel(
                    id = "doesn't matter",
                    label = strings.wikipedia,
                    linkedEntity = MusicBrainzEntity.URL,
                    name = extract.wikipediaUrl,
                    linkedEntityId = "doesn't matter",
                ),
                onItemClick = { _, _, _ ->
                    uriHandler.openUri(extract.wikipediaUrl)
                },
            )
        }
    }
}
