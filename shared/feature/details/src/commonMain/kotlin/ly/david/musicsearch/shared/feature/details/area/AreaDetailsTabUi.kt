package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun AreaDetailsTabUi(
    area: AreaDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val strings = LocalStrings.current

    val entityInfoSection: @Composable AreaDetailsModel.() -> Unit = {
        type?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.type,
                text = it,
                filterText = filterText,
            )
        }
        LifeSpanText(
            lifeSpan = lifeSpan,
            heading = strings.date,
            beginHeading = strings.startDate,
            endHeading = strings.endDate,
            filterText = filterText,
        )
        countryCode.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.iso31661,
                text = it,
                filterText = filterText,
            )
            TextWithHeading(
                heading = strings.regionalIndicatorSymbol,
                text = it.toFlagEmoji(),
                filterText = filterText,
            )
        }

        // TODO: api doesn't seem to include area containment
        //  but we could get its parent area via relations "part of" "backward"
    }
    DetailsTabUi(
        detailsModel = area,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = entityInfoSection,
        onCollapseExpandAliases = onCollapseExpandAliases,
    )
}
