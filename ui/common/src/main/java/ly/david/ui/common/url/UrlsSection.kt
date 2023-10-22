package ly.david.ui.common.url

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.relation.RelationListItem
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun UrlsSection(
    urls: List<RelationListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    urlsSectionViewModel: UrlsSectionViewModel = koinViewModel(),
) {
    Column(modifier = modifier) {
        ListSeparatorHeader("Links")
        urls
            .filter { it.name.contains(filterText) || it.label.contains(filterText) }
            .forEach {
                RelationListItem(
                    relation = it,
                    onItemClick = { entity, id, title ->
                        if (entity == MusicBrainzEntity.URL) {
                            urlsSectionViewModel.openInBrowser(title.orEmpty())
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
                ),
            )
        }
    }
}
