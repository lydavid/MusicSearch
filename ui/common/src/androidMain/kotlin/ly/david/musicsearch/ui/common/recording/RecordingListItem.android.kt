package ly.david.musicsearch.ui.common.recording

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewRecordingListItem() {
    PreviewWithTransitionAndOverlays {
        RecordingListItem(
            recording = RecordingListItemModel(
                id = "1",
                name = "Recording name",
            ),
            filterText = "",
            now = Instant.parse("2025-04-26T16:42:20Z"),
            showLastListenedPeriod = false,
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
                isrcs = persistentListOf("JPVI02601342"),
                formattedArtistCredits = "Some artist feat. Other artist",
                listenCount = 38,
                lastListenedAtMs = 1783121497000,
            ),
            filterText = "feat",
            now = Instant.parse("2026-07-04T16:42:20Z"),
            showLastListenedPeriod = true,
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
            filterText = "",
            now = Instant.parse("2025-04-26T16:42:20Z"),
            showLastListenedPeriod = true,
        )
    }
}
