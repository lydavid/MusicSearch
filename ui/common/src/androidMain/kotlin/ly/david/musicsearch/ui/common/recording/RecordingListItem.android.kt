package ly.david.musicsearch.ui.common.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItem() {
    PreviewWithTransitionAndOverlays {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "1",
                name = "Recording name",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItemAllInfo() {
    PreviewWithTransitionAndOverlays {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "2",
                name = "Recording name",
                firstReleaseDate = "2022-05-23",
                disambiguation = "that one",
                length = 25300000,
                video = true,
                formattedArtistCredits = "Some artist feat. Other artist",
                listenCount = 38,
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItemVisited() {
    PreviewWithTransitionAndOverlays {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "2",
                name = "Recording name",
                firstReleaseDate = "2022-05-23",
                disambiguation = "that one",
                length = 25300000,
                video = true,
                formattedArtistCredits = "Some artist feat. Other artist",
                visited = true,
            ),
        )
    }
}
