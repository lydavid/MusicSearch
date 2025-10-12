package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.theme.TINY_ICON_SIZE
import ly.david.musicsearch.ui.common.theme.TextStyles

@Composable
internal fun SearchHistoryListItem(
    searchHistory: SearchHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entityType: MusicBrainzEntityType, query: String) -> Unit = { _, _ -> },
    onDeleteItem: (SearchHistoryListItemModel) -> Unit = {},
) {
    SwipeToDeleteListItem(
        content = {
            ListItem(
                headlineContent = {
                    Text(
                        text = searchHistory.query,
                        style = TextStyles.getCardBodyTextStyle(),
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                modifier = modifier.clickable {
                    onItemClick(
                        searchHistory.entityType,
                        searchHistory.query,
                    )
                },
                leadingContent = {
                    EntityIcon(
                        entityType = searchHistory.entityType,
                        modifier = Modifier.size(TINY_ICON_SIZE.dp),
                    )
                },
            )
        },
        onDelete = {
            onDeleteItem(searchHistory)
        },
    )
}
