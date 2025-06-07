package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun ReleaseGroupDetailsTabUi(
    releaseGroup: ReleaseGroupDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = releaseGroup,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = {
            TextWithHeading(
                heading = strings.type,
                text = getDisplayTypes(),
                filterText = filterText,
            )
            firstReleaseDate.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.firstReleaseDate,
                    text = it,
                    filterText = filterText,
                )
            }
        },
    )
}
