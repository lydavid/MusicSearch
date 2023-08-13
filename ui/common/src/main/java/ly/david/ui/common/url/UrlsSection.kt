package ly.david.ui.common.url

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.relation.RelationListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@Composable
fun UrlsSection(
    urls: List<RelationListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    Column(modifier = modifier) {
        ListSeparatorHeader("Links")
        urls
            .filter { it.name.contains(filterText) || it.label.contains(filterText) }
            .forEach {
                RelationListItem(
                    relation = it,
                    onItemClick = onItemClick
                )
            }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewUrlsSection() {
    PreviewTheme {
        Surface {
            UrlsSection(
                urls = listOf(
                    RelationListItemModel(
                        id = "1",
                        linkedEntityId = "3",
                        linkedEntity = MusicBrainzEntity.URL,
                        label = "stream for free",
                        name = "https://www.example.com",
                    ),
                    RelationListItemModel(
                        id = "2",
                        linkedEntityId = "4",
                        linkedEntity = MusicBrainzEntity.URL,
                        label = "official homepage",
                        name = "https://www.example.com",
                    ),
                )
            )
        }
    }
}
