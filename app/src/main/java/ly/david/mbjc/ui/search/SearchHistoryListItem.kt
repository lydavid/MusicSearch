package ly.david.mbjc.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.data.domain.listitem.SearchHistoryListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.ui.common.ResourceIcon
import ly.david.ui.common.TINY_ICON_SIZE
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

@Composable
internal fun SearchHistoryListItem(
    searchHistory: SearchHistoryListItemModel,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzResource, query: String) -> Unit = { _, _ -> },
    onDeleteItem: (SearchHistoryListItemModel) -> Unit = {}
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
                    ResourceIcon(
                        resource = searchHistory.entity,
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
private fun Preview() {
    PreviewTheme {
        Surface {
            SearchHistoryListItem(
                searchHistory = SearchHistoryListItemModel(
                    id = "ARTIST_aha",
                    query = "aha",
                    entity = MusicBrainzResource.ARTIST
                )
            )
        }
    }
}
