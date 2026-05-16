package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.releasegroup.getDisplayString
import ly.david.musicsearch.ui.common.screen.NavigatableFromOverlayResult
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.firstReleaseDate
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReleaseGroupDetailsTabUi(
    releaseGroup: ReleaseGroupDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandSection: (CollapsibleSection) -> Unit = {},
    snackbarHostState: SnackbarHostState,
    onGoToScreen: (screen: NavigatableFromOverlayResult) -> Unit,
    onRefreshLocal: () -> Unit,
    onLoginClick: () -> Unit,
) {
    DetailsTabUi(
        detailsModel = releaseGroup,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
        onCollapseExpandSection = onCollapseExpandSection,
        snackbarHostState = snackbarHostState,
        onGoToScreen = onGoToScreen,
        onRefreshLocal = onRefreshLocal,
        onLoginClick = onLoginClick,
        entityInfoSection = {
            TextWithHeading(
                heading = stringResource(Res.string.type),
                text = getDisplayString(),
                filterText = filterText,
            )
            firstReleaseDate.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.firstReleaseDate),
                    text = it,
                    filterText = filterText,
                )
            }
        },
    )
}
