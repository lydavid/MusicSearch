package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.screen.NavigatableFromOverlayResult
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SeriesDetailsTabUi(
    series: SeriesDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandSection: (CollapsibleSection) -> Unit = {},
    snackbarHostState: SnackbarHostState,
    onGoToScreen: (screen: NavigatableFromOverlayResult) -> Unit,
    onRefreshLocal: () -> Unit,
    onLoginClick: () -> Unit,
) {
    DetailsTabUi(
        detailsModel = series,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandSection = onCollapseExpandSection,
        snackbarHostState = snackbarHostState,
        onGoToScreen = onGoToScreen,
        onRefreshLocal = onRefreshLocal,
        onLoginClick = onLoginClick,
        entityInfoSection = {
            type.ifNotEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.type),
                    text = it,
                    filterText = filterText,
                )
            }
        },
    )
}
