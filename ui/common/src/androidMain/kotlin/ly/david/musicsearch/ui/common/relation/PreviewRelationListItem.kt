package ly.david.musicsearch.ui.common.relation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewArtistRelationListItem() {
    PreviewWithTransitionAndOverlays {
        RelationListItem(
            relation = RelationListItemModel(
                id = "2_0",
                linkedEntityId = "2",
                linkedEntity = MusicBrainzEntityType.ARTIST,
                type = "miscellaneous support",
                name = "Artist Name",
                disambiguation = "that guy",
                attributes = "task: director & organizer, strings",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingRelationListItem() {
    PreviewWithTransitionAndOverlays {
        RelationListItem(
            relation = RelationListItemModel(
                id = "2_1",
                linkedEntityId = "2",
                linkedEntity = MusicBrainzEntityType.RECORDING,
                type = "DJ-mixes",
                name = "Recording Name",
                attributes = "number: 10",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingRelationListItemVisited() {
    PreviewWithTransitionAndOverlays {
        RelationListItem(
            relation = RelationListItemModel(
                id = "2_1",
                linkedEntityId = "2",
                linkedEntity = MusicBrainzEntityType.RECORDING,
                type = "DJ-mixes",
                name = "Recording Name",
                attributes = "number: 10",
                visited = true,
            ),
        )
    }
}
