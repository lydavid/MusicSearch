package ly.david.ui.common.url

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.relation.RelationListItem

@Composable
fun UrlsSection(
    urls: List<RelationListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier) {
        ListSeparatorHeader("Links")
        urls
            .filter { it.name.contains(filterText) || it.label.contains(filterText) }
            .forEach {
                RelationListItem(
                    relation = it,
                    onItemClick = { entity, id, title ->
                        if (entity == MusicBrainzEntity.URL) {
                            uriHandler.openUri(title.orEmpty())
                        } else {
                            onItemClick(
                                entity,
                                id,
                                title,
                            )
                        }
                    },
                )
            }
    }
}
