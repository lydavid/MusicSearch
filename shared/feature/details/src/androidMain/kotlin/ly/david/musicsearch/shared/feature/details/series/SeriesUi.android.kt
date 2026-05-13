package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "96b6d0d4-0ca2-408f-9c5f-cb4d6dc5de8e",
        entityType = MusicBrainzEntityType.SERIES,
    ),
    tabs = seriesTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = SeriesDetailsModel(
        id = "96b6d0d4-0ca2-408f-9c5f-cb4d6dc5de8e",
        name = "Classic 100",
        disambiguation = "series of series",
        type = "Series series",
        lastUpdated = Instant.parse("2025-06-05T20:42:20Z"),
    ),
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-06-05T20:42:20Z"),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewSeriesDetailsUi() {
    PreviewWithTransitionAndOverlays {
        SeriesUi(
            state = detailsUiState,
        )
    }
}
