package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.label.LabelType
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "l1",
        entityType = MusicBrainzEntityType.LABEL,
    ),
    tabs = labelTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = LabelDetailsModel(
        id = "f3b662ac-d029-4c87-a9bd-2f2f65b21e4f",
        name = "EMI Classics",
        disambiguation = "absorbed into Warner Classics since 2013-07-19",
        type = LabelType.Production,
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

@PreviewLightDark
@Composable
internal fun PreviewLabelDetailsUi() {
    PreviewWithTransitionAndOverlays {
        LabelUi(
            state = detailsUiState,
        )
    }
}
