package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.TimeZone
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val submitListenType = SubmitListenType.Track(
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
    releaseName = null,
)

// Preview crashes when clicking due to https://issuetracker.google.com/issues/437003350
@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedCustomLocal() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = submitListenType,
                    dateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("America/Toronto"),
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
                    submitListenType = submitListenType,
                    // a new day in UTC, but not in Toronto time
                    dateTimeEpochSeconds = 86400,
                    useCustomTime = true,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
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
                    submitListenType = submitListenType,
                    // a new day in Paris, but not in UTC
                    dateTimeEpochSeconds = 86400 - 3600,
                    eventSink = {},
                ),
                timeZone = TimeZone.of("Europe/Paris"),
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
                    submitListenType = submitListenType,
                    dateTimeEpochSeconds = 86400 - 3600,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
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
                    submitListenType = submitListenType,
                    dateTimeEpochSeconds = 1772841600,
                    timestampIsStartTime = false,
                    eventSink = {},
                ),
                timeZone = TimeZone.UTC,
            )
        }
    }
}
