package ly.david.musicsearch.ui.common.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItem() {
    PreviewWithSharedElementTransition {
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
    PreviewWithSharedElementTransition {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "2",
                name = "Recording name",
                firstReleaseDate = "2022-05-23",
                disambiguation = "that one",
                length = 25300000,
                video = false,
                formattedArtistCredits = "Some artist feat. Other artist",
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItemVisited() {
    PreviewWithSharedElementTransition {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "2",
                name = "Recording name",
                firstReleaseDate = "2022-05-23",
                disambiguation = "that one",
                length = 25300000,
                video = false,
                formattedArtistCredits = "Some artist feat. Other artist",
                visited = true,
            ),
        )
    }
}
