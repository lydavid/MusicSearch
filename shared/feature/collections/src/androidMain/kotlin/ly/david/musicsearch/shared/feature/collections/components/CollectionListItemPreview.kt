package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCollectionListItem(isRemote: Boolean = true) {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collection = CollectionListItemModel(
                    id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                    isRemote = isRemote,
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzEntity.RECORDING,
                    entityCount = 9999,
                ),
            )
        }
    }
}
