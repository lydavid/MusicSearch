package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.track.TrackListItem

@PreviewLightDark
@Composable
internal fun PreviewTrackListItem() {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = TrackListItemModel(
                    id = "1",
                    name = "Track title",
                    position = 1,
                    number = "A",
                    mediumId = 1L,
                    recordingId = "r1",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTrackListItemAllInfo() {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = TrackListItemModel(
                    id = "2",
                    name = "Track title that is long and wraps around",
                    position = 1,
                    number = "123",
                    length = 25300000,
                    mediumId = 2L,
                    recordingId = "r2",
                    formattedArtistCredits = "Some artist feat. Other artist",
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTrackListItemVisited() {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = TrackListItemModel(
                    id = "2",
                    name = "Track title that is long and wraps",
                    position = 1,
                    number = "123",
                    length = 25300000,
                    mediumId = 2L,
                    recordingId = "r2",
                    formattedArtistCredits = "Some artist feat. Other artist",
                    visited = true,
                ),
            )
        }
    }
}
