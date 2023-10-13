package ly.david.musicsearch.feature.search.internal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.SearchHistoryListItemModel
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.core.TINY_ICON_SIZE
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

@Composable
internal fun SearchHistoryListItem(
    searchHistory: SearchHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, query: String) -> Unit = { _, _ -> },
    onDeleteItem: (SearchHistoryListItemModel) -> Unit = {},
) {
    SwipeToDeleteListItem(
        content = {
            ListItem(
                headlineContent = {
                    Text(
                        text = searchHistory.query,
                        style = TextStyles.getCardBodyTextStyle(),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = modifier.clickable {
                    onItemClick(searchHistory.entity, searchHistory.query)
                },
                leadingContent = {
                    EntityIcon(
                        entity = searchHistory.entity,
                        modifier = Modifier.size(TINY_ICON_SIZE.dp)
                    )
                },
            )
        },
        onDelete = {
            onDeleteItem(searchHistory)
        }
    )
}

@DefaultPreviews
@Composable
internal fun PreviewSearchHistoryListItem() {
    PreviewTheme {
        Surface {
            SearchHistoryListItem(
                searchHistory = SearchHistoryListItemModel(
                    id = "ARTIST_aha",
                    query = "aha",
                    entity = MusicBrainzEntity.ARTIST
                )
            )
        }
    }
}
