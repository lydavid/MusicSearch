package ly.david.musicsearch.shared.feature.details.place

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.place.PlaceType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "p1",
        entityType = MusicBrainzEntityType.PLACE,
    ),
    tabs = placeTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = PlaceDetailsModel(
        id = "p1",
        name = "Some Place",
        type = PlaceType.Venue,
        address = "123 Fake St",
        coordinates = CoordinatesUiModel(
            -123.4567,
            123.4567,
        ),
        lifeSpan = LifeSpanUiModel(
            begin = "2022-01-01",
            end = "2022-12-10",
            ended = true,
        ),
        area = AreaListItemModel(
            id = "a1",
            "Area",
            "that one",
        ),
        lastUpdated = Instant.parse("2025-05-05T20:42:20Z"),
    ),
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-06-05T20:42:20Z"),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewPlaceDetailsUi() {
    PreviewWithTransitionAndOverlays {
        PlaceUi(
            state = detailsUiState,
        )
    }
}
