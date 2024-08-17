package ly.david.musicsearch.shared.feature.search

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.search.internal.SearchHistoryListItem
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewSearchHistoryListItem() {
    PreviewTheme {
        Surface {
            SearchHistoryListItem(
                searchHistory = SearchHistoryListItemModel(
                    id = "ARTIST_aha",
                    query = "aha",
                    entity = MusicBrainzEntity.ARTIST,
                ),
            )
        }
    }
}
