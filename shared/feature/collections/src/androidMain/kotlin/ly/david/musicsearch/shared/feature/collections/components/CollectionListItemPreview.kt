package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collection = CollectionListItemModel(
                    id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                    isRemote = true,
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzEntity.RECORDING,
                    entityCount = 9999,
                    entityIds = listOf(
                        "1b1e4b65-9b1a-48cd-8e3a-b4824f15bf0c",
                        "b437fbda-9c32-4078-afa2-1afb98ff0d74",
                    ),
                ),
            )
        }
    }
}
