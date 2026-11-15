package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.recording.RecordingsMatchingIsrcResult
import ly.david.musicsearch.ui.common.preview.PreviewTheme
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentManual() {
    PreviewTheme {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.Loading,
                    isAutomatic = false,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentLoading() {
    PreviewTheme {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.Loading,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentNoUnmappedIsrc() {
    PreviewTheme {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.NoUnmappedIsrc,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentNoMatchingRecordings() {
    PreviewTheme {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.NoMatchingRecordings,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentNetworkError() {
    PreviewTheme {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.NetworkError(
                        message = "Some network error",
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentSuccess() {
    PreviewWithTransitionAndOverlays {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.Success(
                        recordings = listOf(
                            RecordingListItemModel(
                                id = "4ea82cee-e79f-4721-8d5e-adeab3f43a2b",
                                name = "Klaviersonate Nr. 14 cis-Moll, Op. 27 Nr. 2 \"Mondschein\": " +
                                    "I. Adagio sostenuto",
                                disambiguation = "",
                                firstReleaseDate = "1984-01-01",
                                length = 397773,
                                isrcs = persistentListOf("DEF058400540", "DEF058400541"),
                                formattedArtistCredits = "Daniel Barenboim",
                                visited = true,
                            ),
                        ),
                    ),
                ),
            )
        }
    }
}

// assuming the listen had ISRC DEF058400540, but they actually use the other distinct ISRC
@PreviewLightDark
@Composable
internal fun PreviewLinkWithMusicBrainzDialogContentSuccessMultiple() {
    PreviewWithTransitionAndOverlays {
        Surface {
            LinkWithMusicBrainzDialogContent(
                linkUiState = LinkUiState(
                    recordingsMatchingIsrcResult = RecordingsMatchingIsrcResult.Success(
                        recordings = listOf(
                            RecordingListItemModel(
                                id = "4ea82cee-e79f-4721-8d5e-adeab3f43a2b",
                                name = "Klaviersonate Nr. 14 cis-Moll, Op. 27 Nr. 2 \"Mondschein\": " +
                                    "I. Adagio sostenuto",
                                disambiguation = "",
                                firstReleaseDate = "1984-01-01",
                                length = 397773,
                                isrcs = persistentListOf("DEF058400540", "DEF058400541"),
                                formattedArtistCredits = "Daniel Barenboim",
                                visited = true,
                            ),
                            RecordingListItemModel(
                                id = "620f4c17-88dd-4441-a5b0-7edcb40d3995",
                                name = "Piano Sonata no. 14 in C-sharp minor, op. 27 no. 2 \"Moonlight\": " +
                                    "II. Allegretto - attacca",
                                disambiguation = "",
                                firstReleaseDate = "1984-01-01",
                                length = 132706,
                                isrcs = persistentListOf("DEF058400540", "DEF058400542"),
                                formattedArtistCredits = "Daniel Barenboim",
                                visited = false,
                            ),
                            RecordingListItemModel(
                                id = "be8486b7-305a-4142-adf1-390ad3cb5f95",
                                name = "Piano Sonata no. 14 in C-sharp minor, op. 27 no. 2 \"Moonlight\": " +
                                    "III. Presto agitato",
                                disambiguation = "",
                                firstReleaseDate = "1984-01-01",
                                length = 468440,
                                isrcs = persistentListOf("DEF058400540", "DEF058400543"),
                                formattedArtistCredits = "Daniel Barenboim",
                                visited = false,
                                video = true,
                            ),
                        ),
                    ),
                ),
            )
        }
    }
}
