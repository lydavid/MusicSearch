package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

// region Previews
@PreviewLightDark
@Composable
internal fun PreviewEventDetailsUi() {
    PreviewWithTransitionAndOverlays {
        EventDetailsTabUi(
            event = EventDetailsModel(
                id = "e1",
                name = "Some Place",
                type = "Festival",
                time = "13:00",
                cancelled = true,
                lifeSpan = LifeSpanUiModel(
                    begin = "2022-01-01",
                    end = "2022-12-10",
                    ended = true,
                ),
            ),
            detailsTabUiState = DetailsTabUiState(
                numberOfImages = 0,
            ),
        )
    }
}
// endregion
