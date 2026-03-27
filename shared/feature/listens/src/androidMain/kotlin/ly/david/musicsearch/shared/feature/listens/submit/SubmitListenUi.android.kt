package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.shared.domain.listen.TrackInfo
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import kotlin.time.Clock

private val artists = persistentListOf(
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
)
private val trackSubmitListenType = SubmitListenType.Track(
    info = TrackInfo(
        name = "Track",
        disambiguation = "That one",
        aliases = persistentListOf(
            BasicAlias(
                name = "English name",
                locale = "en",
                isPrimary = true,
            ),
        ),
        recordingId = "t1",
        lengthMilliseconds = 275186,
        artists = artists,
    ),
    releaseName = "Album name",
    releaseId = "r1",
)

// Preview crashes when clicking due to https://issuetracker.google.com/issues/437003350
@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedCustomLocal() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    dateTimeEpochSeconds = 86400,
                    listenedAtDateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("America/Toronto"),
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedCustomUTC() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    // a new day in UTC, but not in Toronto time
                    dateTimeEpochSeconds = 86400,
                    listenedAtDateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedNowLocal() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    // a new day in Paris, but not in UTC
                    dateTimeEpochSeconds = 86400 - 3600,
                    listenedAtDateTimeEpochSeconds = 86400 - 3600,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("Europe/Paris"),
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedNowUTC() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    dateTimeEpochSeconds = 86400 - 3600,
                    listenedAtDateTimeEpochSeconds = 86400 - 3600,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiFinished() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    dateTimeEpochSeconds = 1772841600,
                    listenedAtDateTimeEpochSeconds = 1772841600L - (
                        trackSubmitListenType.info.lengthMilliseconds
                            ?: 0
                        ) / MS_IN_SECOND,
                    timestampIsStartTime = false,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedCustomLocalDaylightSaving() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType,
                    dateTimeEpochSeconds = 1772953200,
                    listenedAtDateTimeEpochSeconds = 1772953200,
                    useCustomTime = true,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("America/Toronto"),
                clock = Clock.System,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiFinishedCustomLocalWithoutAlbum() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = trackSubmitListenType.copy(
                        releaseName = null,
                        releaseId = null,
                    ),
                    dateTimeEpochSeconds = 86400,
                    listenedAtDateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    timestampIsStartTime = false,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("America/Toronto"),
                clock = Clock.System,
            )
        }
    }
}

private val albumSubmitListenType = SubmitListenType.Album(
    releaseName = "Album name",
    releaseId = "r1",
    releaseArtists = artists,
    recordingIds = persistentListOf("rec2", "rec3", "rec4"),
)

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiAlbumTypeFinishedCustom() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = albumSubmitListenType,
                    dateTimeEpochSeconds = 86400,
                    // This would need to be calculated by the presenter, so it's incorrect here.
                    listenedAtDateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    timestampIsStartTime = false,
                    allSelectedTrackInfo = listOf(
                        TrackInfo(
                            name = "Track 2",
                            recordingId = "rec2",
                            lengthMilliseconds = 400L * MS_IN_SECOND,
                            artists = artists,
                            disambiguation = null,
                            aliases = persistentListOf(),
                        ),
                        TrackInfo(
                            name = "Track 3",
                            recordingId = "rec3",
                            lengthMilliseconds = 4000L * MS_IN_SECOND,
                            artists = artists,
                            disambiguation = null,
                            aliases = persistentListOf(),
                        ),
                        TrackInfo(
                            name = "Track 4",
                            recordingId = "rec4",
                            lengthMilliseconds = null,
                            artists = artists,
                            disambiguation = null,
                            aliases = persistentListOf(),
                        ),
                    ),
                    eventSink = {},
                ),
                timeZone = TimeZone.of("America/Toronto"),
                clock = Clock.System,
            )
        }
    }
}
