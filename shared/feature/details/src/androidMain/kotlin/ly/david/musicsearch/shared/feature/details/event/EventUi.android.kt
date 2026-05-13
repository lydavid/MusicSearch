package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

// region Previews
private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "e1",
        entityType = MusicBrainzEntityType.EVENT,
    ),
    tabs = eventTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = EventDetailsModel(
        id = "e1",
        name = "Some Event",
        type = "Festival",
        time = "13:00",
        cancelled = true,
        lifeSpan = LifeSpanUiModel(
            begin = "2022-01-01",
            end = "2022-12-10",
            ended = true,
        ),
        lastUpdated = Instant.parse("2022-06-05T20:42:20Z"),
    ),
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-06-05T20:42:20Z"),
        numberOfImages = 0,
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewEventDetailsUi() {
    PreviewWithTransitionAndOverlays {
        EventUi(
            state = detailsUiState,
        )
    }
}
// endregion
