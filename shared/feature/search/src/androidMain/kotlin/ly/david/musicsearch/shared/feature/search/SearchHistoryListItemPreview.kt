package ly.david.musicsearch.shared.feature.search

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.theme.PreviewTheme

@PreviewLightDark
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
