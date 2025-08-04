package ly.david.musicsearch.shared.feature.details.series

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.SeriesDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun SeriesDetailsTabUi(
    series: SeriesDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = series,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        onCollapseExpandAliases = onCollapseExpandAliases,
        entityInfoSection = {
            type?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.type,
                    text = it,
                    filterText = filterText,
                )
            }
        },
    )
}
