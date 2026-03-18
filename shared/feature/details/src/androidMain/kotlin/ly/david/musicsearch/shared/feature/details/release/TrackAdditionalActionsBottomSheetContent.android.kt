package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewTrackAdditionalActionsBottomSheetContentLong() {
    PreviewWithTransitionAndOverlays {
        Surface {
            TrackAdditionalActionsBottomSheetContent(
                track = TrackListItemModel(
                    id = "2",
                    position = 1,
                    number = "123",
                    name = "Track title that is long and wraps around",
                    length = 25300000,
                    mediumId = 2L,
                    recordingId = "r2",
                    artists = persistentListOf(
                        ArtistCreditUiModel(
                            artistId = "a1",
                            name = "Artist",
                            joinPhrase = "  feat. ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "a2",
                            name = "Another artist",
                            joinPhrase = "",
                        ),
                    ),
                    mediumPosition = 1,
                    listenCount = 3,
                    aliases = persistentListOf(
                        BasicAlias(
                            name = "English name",
                            locale = "en",
                            isPrimary = true,
                        ),
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewTrackAdditionalActionsBottomSheetContent() {
    PreviewWithTransitionAndOverlays {
        Surface {
            TrackAdditionalActionsBottomSheetContent(
                track = TrackListItemModel(
                    id = "2",
                    position = 1,
                    number = "2",
                    name = "Track title",
                    length = 253000,
                    mediumId = 2L,
                    recordingId = "r2",
                    artists = persistentListOf(
                        ArtistCreditUiModel(
                            artistId = "a1",
                            name = "Artist",
                            joinPhrase = "",
                        ),
                    ),
                    mediumPosition = 1,
                    listenCount = 3,
                ),
            )
        }
    }
}
