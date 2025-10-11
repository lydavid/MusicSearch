package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import kotlin.time.Instant

@PreviewLightDark
@Composable
internal fun PreviewLabelDetailsUi() {
    PreviewWithTransitionAndOverlays {
        LabelDetailsTabUi(
            label = LabelDetailsModel(
                id = "f3b662ac-d029-4c87-a9bd-2f2f65b21e4f",
                name = "EMI Classics",
                disambiguation = "absorbed into Warner Classics since 2013-07-19",
                type = "Production",
                labelCode = 6646,
                lifeSpan = LifeSpanUiModel(
                    begin = "1990",
                    end = "2013-07-19",
                    ended = true,
                ),
                lastUpdated = Instant.fromEpochSeconds(1756695182),
            ),
            detailsTabUiState = DetailsTabUiState(
                now = Instant.fromEpochSeconds(1756695182),
            ),
        )
    }
}
