package ly.david.musicsearch.ui.common.track

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewTrackListItem() {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = TrackListItemModel(
                    id = "1",
                    position = 1,
                    number = "A",
                    name = "Track title",
                    mediumId = 1L,
                    recordingId = "r1",
                    mediumPosition = 1,
                ),
                filterText = "",
                mostListenedTrackCount = 0,
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
                            joinPhrase = " feat. ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "a2",
                            name = "Another Artist",
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
                    collected = true,
                ),
                filterText = "a",
                mostListenedTrackCount = 5,
            )
        }
    }
}

// Icon seems to scale properly live.
@PreviewLightDark
@Composable
internal fun PreviewTrackListItemVisited() {
    PreviewTheme {
        Surface {
            TrackListItem(
                track = TrackListItemModel(
                    id = "2",
                    position = 1,
                    number = "123",
                    name = "Track title that is long and wraps",
                    length = 25300000,
                    mediumId = 2L,
                    recordingId = "r2",
                    artists = persistentListOf(
                        ArtistCreditUiModel(
                            artistId = "a1",
                            name = "Artist",
                            joinPhrase = " feat. ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "a2",
                            name = "Another Artist",
                            joinPhrase = "",
                        ),
                    ),
                    visited = true,
                    mediumPosition = 1,
                ),
                filterText = "",
                mostListenedTrackCount = 0,
            )
        }
    }
}
