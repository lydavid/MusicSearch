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
