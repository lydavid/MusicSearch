package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun LabelDetailsTabUi(
    label: LabelDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = label,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = {
            type?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.type,
                    text = it,
                    filterText = filterText,
                )
            }
            labelCode?.ifNotNull {
                TextWithHeading(
                    heading = strings.labelCode,
                    text = strings.lc(it),
                    filterText = filterText,
                )
            }

            ipis?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.ipi,
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }

            isnis?.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.isni,
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }

            // TODO: lifespan, founded, defunct for end

            // TODO: area
        },
    )
}
