package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContent() {
    PreviewWithTransitionAndOverlays {
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
                filterText = "",
                allowedToEdit = false,
                filteringByThisRecording = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentAlternative() {
    PreviewWithTransitionAndOverlays {
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
                filterText = "sound",
                allowedToEdit = true,
                filteringByThisRecording = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentUnlinked() {
    PreviewWithTransitionAndOverlays {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                ),
                filterText = "",
                allowedToEdit = false,
                filteringByThisRecording = false,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentFilteringByUnlinked() {
    PreviewWithTransitionAndOverlays {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = ListenListItemModel(
                    listenedAtMs = 1755645177000,
                    username = "user",
                    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                    name = "Color Your Night",
                    formattedArtistCredits = "Lotus Juice & 高橋あず美",
                ),
                filterText = "",
                allowedToEdit = false,
                filteringByThisRecording = true,
            )
        }
    }
}
