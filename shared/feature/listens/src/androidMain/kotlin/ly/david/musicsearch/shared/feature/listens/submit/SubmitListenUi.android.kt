package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
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
            joinPhrase = " feat. "
        ),
        ArtistCreditUiModel(
            artistId = "a2",
            name = "Another Artist",
            joinPhrase = "",
        )
    ),
    releaseName = null,
)

@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStarted() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = submitListenType,
                    dateTimestampSeconds = 1772755200,
                    timeTimestampSeconds = 85712,
                    eventSink = {},
                ),
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
                    dateTimestampSeconds = 0,
                    timeTimestampSeconds = 0,
                    timestampIsStartTime = false,
                    eventSink = {},
                ),
            )
        }
    }
}

// Preview crashes when clicking due to https://issuetracker.google.com/issues/437003350
@PreviewLightDark
@Composable
internal fun PreviewSubmitListenUiStartedCustom() {
    PreviewTheme {
        Surface {
            SubmitListenUi(
                state = SubmitListenUiState(
                    submitListenType = submitListenType,
                    dateTimestampSeconds = 0,
                    timeTimestampSeconds = 0,
                    isCustomTime = true,
                    eventSink = {},
                ),
            )
        }
    }
}
