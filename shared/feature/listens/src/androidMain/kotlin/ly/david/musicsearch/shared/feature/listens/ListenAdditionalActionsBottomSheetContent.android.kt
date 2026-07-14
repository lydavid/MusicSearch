package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

private val listen = ListenListItemModel(
    listenedAtMs = 1755645177000,
    username = "user",
    recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
    recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
    recordingName = "Color Your Night",
    recordingDurationMs = 227240,
    unmappedDurationMs = 228240,
    unmappedTrackName = "COLOR YOUR NIGHT",
    separateArtistCredits = persistentListOf(
        ArtistCreditUiModel(
            artistId = "c731e592-2620-4f4c-859d-39e294b06b35",
            name = "Lotus Juice",
            joinPhrase = " & ",
        ),
        ArtistCreditUiModel(
            artistId = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
            name = "高橋あず美",
            joinPhrase = "",
        ),
    ),
    unmappedFormattedArtistCredits = "Lotus Juice, 高橋あず美, アトラスサウンドチーム, ATLUS GAME MUSIC",
    release = ListenRelease(
        id = "6ad6af61-7e68-472a-9f9d-0030781ad964",
        mappedName = "PERSONA3 RELOAD Limited Box Original Soundtrack",
        unmappedName = "Persona 3 Reload Original Soundtrack",
    ),
    unmappedDiscNumber = 1,
    unmappedTrackNumber = "16",
    unmappedIsrc = "JPK652300116",
    isrcs = persistentListOf("JPK652300116"),
)

private val unlinkedListen = listen.copy(
    recordingId = "",
    recordingName = null,
    recordingDurationMs = null,
    separateArtistCredits = persistentListOf(),
    release = ListenRelease(
        unmappedName = "Persona 3 Reload Original Soundtrack",
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContent() {
    PreviewWithTransitionAndOverlays {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = listen,
                filterText = "",
                showUnmappedData = false,
                allowedToEdit = false,
                selectedEntityFacet = null,
                filteringByThisDate = false,
                boldUnvisited = true,
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
                listen = listen,
                filterText = "sound",
                showUnmappedData = true,
                allowedToEdit = true,
                selectedEntityFacet = MusicBrainzEntity(
                    type = MusicBrainzEntityType.RECORDING,
                    id = "a",
                ),
                showMoreFilters = true,
                filteringByThisDate = false,
                boldUnvisited = false,
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
                listen = unlinkedListen,
                filterText = "",
                showUnmappedData = false,
                allowedToEdit = false,
                selectedEntityFacet = null,
                showMoreFilters = true,
                filteringByThisDate = false,
                boldUnvisited = true,
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
                listen = unlinkedListen,
                filterText = "",
                showUnmappedData = false,
                allowedToEdit = false,
                selectedEntityFacet = MusicBrainzEntity(
                    type = MusicBrainzEntityType.RECORDING,
                    id = "",
                ),
                showMoreFilters = true,
                filteringByThisDate = false,
                boldUnvisited = true,
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewListensUiListenAdditionalActionsBottomSheetContentUnlinkedFilteringByDate() {
    PreviewWithTransitionAndOverlays {
        Surface {
            ListenAdditionalActionsBottomSheetContent(
                listen = unlinkedListen,
                filterText = "",
                showUnmappedData = false,
                allowedToEdit = false,
                selectedEntityFacet = null,
                filteringByThisDate = true,
                boldUnvisited = true,
            )
        }
    }
}
