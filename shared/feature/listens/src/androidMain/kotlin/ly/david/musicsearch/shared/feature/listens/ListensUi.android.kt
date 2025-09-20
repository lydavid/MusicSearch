package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.Identifiable
import ly.david.musicsearch.shared.domain.common.getDateFormatted
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.recording.RecordingFacet
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewListensUi() {
    PreviewWithSharedElementTransition {
        Surface {
            val listens = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        ListSeparator(
                            id = 1755655177000.toString(),
                            text = Instant.fromEpochMilliseconds(1755655177000).getDateFormatted(),
                        ),
                        ListenListItemModel(
                            listenedAtMs = 1755655177000,
                            username = "user",
                            recordingMessybrainzId = "bf2c5a43-19d8-46f7-8131-df986ed24845",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            durationMs = null,
                        ),
                        ListenListItemModel(
                            listenedAtMs = 1755645177000,
                            username = "user",
                            recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            durationMs = 227240,
                            visited = true,
                        ),
                    ),
                ),
            )
            ListensUi(
                state = ListensUiState(
                    username = "user",
                    listensPagingDataFlow = listens,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiWithRecordingFacet() {
    PreviewWithSharedElementTransition {
        Surface {
            val listens = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        ListSeparator(
                            id = 1755655177000.toString(),
                            text = Instant.fromEpochMilliseconds(1755655177000).getDateFormatted(),
                        ),
                        ListenListItemModel(
                            listenedAtMs = 1755645177000,
                            username = "user",
                            recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            durationMs = 227240,
                            visited = true,
                        ),
                    ),
                ),
            )
            ListensUi(
                state = ListensUiState(
                    username = "user",
                    listensPagingDataFlow = listens,
                    recordingFacetUiState = RecordingFacetUiState(
                        selectedRecordingFacetId = "2",
                    ),
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiNoUsername() {
    PreviewWithSharedElementTransition {
        Surface {
            val listens = MutableStateFlow(
                PagingData.from(
                    data = emptyList<Identifiable>(),
                ),
            )
            ListensUi(
                state = ListensUiState(
                    listensPagingDataFlow = listens,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContent() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                    recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                    release = ListenRelease(
                        id = "6ad6af61-7e68-472a-9f9d-0030781ad964",
                        name = "PERSONA3 RELOAD Limited Box Original Soundtrack",
                    ),
                ),
                allowedToEdit = false,
                filteringByThisRecording = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentAlternative() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                    recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                    release = ListenRelease(
                        id = "6ad6af61-7e68-472a-9f9d-0030781ad964",
                        name = "PERSONA3 RELOAD Limited Box Original Soundtrack",
                        visited = true,
                    ),
                ),
                allowedToEdit = true,
                filteringByThisRecording = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentUnlinked() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                ),
                allowedToEdit = false,
                filteringByThisRecording = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentFilteringByUnlinked() {
    PreviewWithSharedElementTransition {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                ),
                allowedToEdit = false,
                filteringByThisRecording = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiRecordingFacetBottomSheetContent() {
    PreviewWithSharedElementTransition {
        Surface {
            val recordingFacets = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        RecordingFacet(
                            id = "",
                            name = "",
                            formattedArtistCredits = "",
                            count = 123,
                        ),
                        RecordingFacet(
                            id = "1",
                            name = "COLORS",
                            formattedArtistCredits = "FLOW",
                            count = 12,
                        ),
                        RecordingFacet(
                            id = "2",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            count = 9,
                        ),
                    ),
                ),
            )
            RecordingFacetBottomSheetContent(
                recordingFacetUiState = RecordingFacetUiState(
                    selectedRecordingFacetId = "2",
                    query = "Color",
                    recordingFacetsPagingDataFlow = recordingFacets,
                ),
            )
        }
    }
}
