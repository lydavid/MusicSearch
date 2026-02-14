package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
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
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    DetailsTabUi(
        detailsModel = releaseGroup,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        onCollapseExpandAliases = onCollapseExpandAliases,
        entityInfoSection = {
            TextWithHeading(
                heading = stringResource(Res.string.type),
                text = getDisplayTypes(),
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
