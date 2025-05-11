package ly.david.musicsearch.ui.common.url

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewUrlsSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                urlsSection(
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
                        RelationListItemModel(
                            id = "2",
                            linkedEntityId = "4",
                            linkedEntity = MusicBrainzEntity.URL,
                            label = "Wikidata",
                            name = "https://www.wikidata.org/wiki/Q719120",
                        ),
                    ),
                )
            }
        }
    }
}
