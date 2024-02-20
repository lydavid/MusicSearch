package ly.david.ui.common.url

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.listitem.RelationListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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
