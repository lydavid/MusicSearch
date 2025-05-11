package ly.david.musicsearch.ui.common.url

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.UrlListItem

fun LazyListScope.urlsSection(
    urls: List<RelationListItemModel>,
) {
    urls.ifNotNullOrEmpty {
        item {
            ListSeparatorHeader("External links")
        }
    }
    items(urls) {
        UrlListItem(
            relation = it,
        )
    }
}
