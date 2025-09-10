package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewUrlsSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                urlsSection(
                    filteredUrls = persistentListOf(
                        RelationListItemModel(
                            id = "1",
                            linkedEntityId = "3",
                            linkedEntity = MusicBrainzEntityType.URL,
                            type = "stream for free",
                            name = "https://www.example.com",
                        ),
                        RelationListItemModel(
                            id = "2",
                            linkedEntityId = "4",
                            linkedEntity = MusicBrainzEntityType.URL,
                            type = "official homepage",
                            name = "https://www.example.com",
                        ),
                        RelationListItemModel(
                            id = "2",
                            linkedEntityId = "4",
                            linkedEntity = MusicBrainzEntityType.URL,
                            type = "Wikidata",
                            name = "https://www.wikidata.org/wiki/Q719120",
                        ),
                    ),
                    totalUrls = 3,
                )
            }
        }
    }
}
