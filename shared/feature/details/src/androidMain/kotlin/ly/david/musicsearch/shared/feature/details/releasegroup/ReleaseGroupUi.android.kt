package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.listen.ListenWithRecording
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val detailsTabUiState = DetailsTabUiState(
    numberOfImages = 1,
    now = Instant.parse("2025-06-05T19:42:20Z"),
    wikipediaExtract = WikipediaExtract(
        extract = "\"Under Pressure\" is a song by the British rock band Queen and " +
            "singer David Bowie. Originally released as a single in October 1981, " +
            "it was later included on Queen's 1982 album Hot Space. The song reached " +
            "number one on the UK Singles Chart, becoming Queen's second number-one hit " +
            "in their home country and Bowie's third, and also charted in the top 10 in " +
            "more than 10 countries around the world.",
        wikipediaUrl = "https://en.wikipedia.org/wiki/Under_Pressure",
    ),
)

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "rg1",
        entityType = MusicBrainzEntityType.RELEASE_GROUP,
    ),
    tabs = releaseGroupTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = ReleaseGroupDetailsModel(
        id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
        name = "Under Pressure",
        primaryType = ReleaseGroupPrimaryType.Single,
        firstReleaseDate = "1981-10",
        lastUpdated = Instant.parse("2024-06-05T19:42:20Z"),
        urls = persistentListOf(
            RelationListItemModel(
                id = "1",
                type = "Discogs",
                name = "https://www.discogs.com/master/13442",
                linkedEntity = MusicBrainzEntityType.URL,
                linkedEntityId = "1",
            ),
            RelationListItemModel(
                id = "2",
                type = "Discogs",
                name = "https://www.discogs.com/master/66626",
                linkedEntity = MusicBrainzEntityType.URL,
                linkedEntityId = "2",
            ),
            RelationListItemModel(
                id = "3",
                type = "Wikidata",
                name = "https://www.wikidata.org/wiki/Q836667",
                linkedEntity = MusicBrainzEntityType.URL,
                linkedEntityId = "3",
            ),
        ),
    ),
    detailsTabUiState = detailsTabUiState,
)

private val detailsUiStateWithListens = detailsUiState.copy(
    detailsModel = detailsUiState.detailsModel?.copy(
        id = "88ee1a12-f362-4737-b7f4-b2f0b079e218",
        name = "幻燈",
        primaryType = ReleaseGroupPrimaryType.Album,
        firstReleaseDate = "2023-04-05",
        listenCount = 134L,
        latestListens = persistentListOf(
            ListenWithRecording(
                id = "c43037a7-e888-49d6-89b5-243c971ca9b2",
                name = "又三郎",
                disambiguation = "",
                listenedMs = 1788520134000L,
            ),
            ListenWithRecording(
                id = "e3c81234-74f9-41db-b51f-f996b02322ea",
                name = "アルジャーノン",
                disambiguation = "",
                listenedMs = 1787678804000L,
            ),
            ListenWithRecording(
                id = "80db4119-213c-4b0c-af9b-37c4a11eaa72",
                name = "老人と海",
                disambiguation = "",
                listenedMs = 1787626640000L,
            ),
            ListenWithRecording(
                id = "9c4a772f-52f5-4472-84dd-35171106ef48",
                name = "第一夜",
                disambiguation = "",
                listenedMs = 1787623106000L,
            ),
        ),
        listenBrainzUrl = "https://listenbrainz.example.com/release-group/88ee1a12-f362-4737-b7f4-b2f0b079e218",
    ),
    detailsTabUiState = detailsUiState.detailsTabUiState.copy(
        now = Instant.parse("2026-09-05T19:42:20Z"),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUiWithListens() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupUi(
            state = detailsUiStateWithListens,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUi() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupUi(
            state = detailsUiState,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUiCollapsed() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupUi(
            state = detailsUiState.copy(
                detailsTabUiState = detailsTabUiState.copy(
                    isSectionCollapsed = persistentSetOf(
                        CollapsibleSection.Information,
                        CollapsibleSection.Wikipedia,
                        CollapsibleSection.ExternalLinks,
                        CollapsibleSection.Aliases,
                    ),
                ),
            ),
        )
    }
}
