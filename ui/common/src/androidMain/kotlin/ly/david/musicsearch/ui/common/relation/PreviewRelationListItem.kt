package ly.david.musicsearch.ui.common.relation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewArtistRelationListItem() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_0",
                    linkedEntityId = "2",
                    linkedEntity = MusicBrainzEntity.ARTIST,
                    label = "miscellaneous support",
                    name = "Artist Name",
                    disambiguation = "that guy",
                    attributes = "task: director & organizer, strings",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingRelationListItem() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedEntityId = "2",
                    linkedEntity = MusicBrainzEntity.RECORDING,
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingRelationListItemVisited() {
    PreviewTheme {
        Surface {
            RelationListItem(
                relation = RelationListItemModel(
                    id = "2_1",
                    linkedEntityId = "2",
                    linkedEntity = MusicBrainzEntity.RECORDING,
                    label = "DJ-mixes",
                    name = "Recording Name",
                    additionalInfo = "by Artist Names (order: 10)",
                    visited = true,
                ),
            )
        }
    }
}
