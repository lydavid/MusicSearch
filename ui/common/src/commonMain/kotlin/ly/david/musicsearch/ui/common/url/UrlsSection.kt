package ly.david.musicsearch.ui.common.url

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem

@Composable
fun UrlsSection(
    urls: List<RelationListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
) {
    Column(modifier = modifier) {
        ListSeparatorHeader("External links")
        urls
            .filter {
                it.name.contains(filterText, ignoreCase = true) ||
                    it.label.contains(filterText, ignoreCase = true)
            }
            .forEach {
                UrlListItem(
                    relation = it,
                )
            }
    }
}
