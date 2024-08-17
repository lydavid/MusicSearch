package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.track.TrackListItem
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

internal class TrackCardPreviewParameterProvider : PreviewParameterProvider<TrackListItemModel> {
    override val values = sequenceOf(
        TrackListItemModel(
            id = "1",
            title = "Track title",
            position = 1,
            number = "A",
            mediumId = 1L,
            recordingId = "r1",
        ),
        TrackListItemModel(
            id = "2",
            title = "Track title that is long and wraps",
            position = 1,
            number = "123",
            length = 25300000,
            mediumId = 2L,
            recordingId = "r2",
            formattedArtistCredits = "Some artist feat. Other artist",
        ),
    )
}

@DefaultPreviews
@Composable
private fun Preview(
    @PreviewParameter(TrackCardPreviewParameterProvider::class) track: TrackListItemModel,
) {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = track,
            )
        }
    }
}
