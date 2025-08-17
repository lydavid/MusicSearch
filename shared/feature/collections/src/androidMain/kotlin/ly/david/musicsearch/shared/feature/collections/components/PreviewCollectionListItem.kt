package ly.david.musicsearch.shared.feature.collections.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewCollectionListItem(
    isRemote: Boolean = true,
    visited: Boolean = true,
    collected: Boolean = true,
) {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collection = CollectionListItemModel(
                    id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                    isRemote = isRemote,
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzEntityType.RECORDING,
                    cachedEntityCount = 9999,
                    visited = visited,
                    containsEntity = collected,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewCollectionListItemDisabled() {
    PreviewTheme {
        Surface {
            CollectionListItem(
                collection = CollectionListItemModel(
                    id = "3048448c-0605-494a-9e9f-c1a0521906f1",
                    isRemote = true,
                    name = "My collection with a very long title",
                    description = "Some songs",
                    entity = MusicBrainzEntityType.RECORDING,
                    cachedEntityCount = 9999,
                    visited = true,
                    containsEntity = false,
                ),
                enabled = false,
            )
        }
    }
}
