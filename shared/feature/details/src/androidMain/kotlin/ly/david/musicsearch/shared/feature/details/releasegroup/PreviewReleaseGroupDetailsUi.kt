package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

private val releaseGroup = ReleaseGroupDetailsModel(
    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
    name = "Under Pressure",
    primaryType = "Single",
    firstReleaseDate = "1981-10",
    lastUpdated = Instant.parse("2024-06-05T19:42:20Z"),
    wikipediaExtract = WikipediaExtract(
        extract = "\"Under Pressure\" is a song by the British rock band Queen and " +
            "singer David Bowie. Originally released as a single in October 1981, " +
            "it was later included on Queen's 1982 album Hot Space. The song reached " +
            "number one on the UK Singles Chart, becoming Queen's second number-one hit " +
            "in their home country and Bowie's third, and also charted in the top 10 in " +
            "more than 10 countries around the world.",
        wikipediaUrl = "https://en.wikipedia.org/wiki/Under_Pressure",
    ),
    urls = listOf(
        RelationListItemModel(
            id = "1",
            label = "Discogs",
            name = "https://www.discogs.com/master/13442",
            linkedEntity = MusicBrainzEntity.URL,
            linkedEntityId = "1",
        ),
        RelationListItemModel(
            id = "2",
            label = "Discogs",
            name = "https://www.discogs.com/master/66626",
            linkedEntity = MusicBrainzEntity.URL,
            linkedEntityId = "2",
        ),
        RelationListItemModel(
            id = "3",
            label = "Wikidata",
            name = "https://www.wikidata.org/wiki/Q836667",
            linkedEntity = MusicBrainzEntity.URL,
            linkedEntityId = "3",
        ),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUi() {
    PreviewWithSharedElementTransition {
        ReleaseGroupDetailsTabUi(
            releaseGroup = releaseGroup,
            detailsTabUiState = DetailsTabUiState(
                numberOfImages = 1,
                now = Instant.parse("2025-06-05T19:42:20Z"),
            ),
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUiCollapsed() {
    PreviewWithSharedElementTransition {
        ReleaseGroupDetailsTabUi(
            releaseGroup = releaseGroup,
            detailsTabUiState = DetailsTabUiState(
                numberOfImages = 1,
                now = Instant.parse("2025-06-05T19:42:20Z"),
                isExternalLinksCollapsed = true,
            ),
        )
    }
}
