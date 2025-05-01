package ly.david.musicsearch.ui.common.relation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewUrlListItem() {
    PreviewTheme {
        Surface {
            UrlListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedEntityId = "3",
                    linkedEntity = MusicBrainzEntity.URL,
                    label = "Stream for free",
                    name = "https://www.example.com",
                    visited = true,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewUrlListItemWikipedia() {
    PreviewTheme {
        Surface {
            UrlListItem(
                relation = RelationListItemModel(
                    id = "wikipedia_section",
                    linkedEntityId = "wikipedia_section",
                    linkedEntity = MusicBrainzEntity.URL,
                    label = "Wikipedia",
                    name = "https://en.wikipedia.org/wiki/Creepy_Nuts",
                    visited = true,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewUrlListItemWikidata() {
    PreviewTheme {
        Surface {
            UrlListItem(
                relation = RelationListItemModel(
                    id = "5a8390ae-65bf-49cf-8677-ff18df336c81_50",
                    linkedEntityId = "5a8390ae-65bf-49cf-8677-ff18df336c81",
                    linkedEntity = MusicBrainzEntity.URL,
                    label = "Wikidata",
                    name = "https://www.wikidata.org/wiki/Q20039817",
                    visited = true,
                ),
            )
        }
    }
}
