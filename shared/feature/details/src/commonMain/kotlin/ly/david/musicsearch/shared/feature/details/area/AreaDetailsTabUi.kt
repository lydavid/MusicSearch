package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.common.toFlagEmoji
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.endDate
import musicsearch.ui.common.generated.resources.iso31661
import musicsearch.ui.common.generated.resources.regionalIndicatorSymbol
import musicsearch.ui.common.generated.resources.startDate
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AreaDetailsTabUi(
    area: AreaDetailsModel,
    detailsTabUiState: DetailsTabUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val entityInfoSection: @Composable AreaDetailsModel.() -> Unit = {
        type.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.type),
                text = it,
                filterText = filterText,
            )
        }
        LifeSpanText(
            lifeSpan = lifeSpan,
            heading = stringResource(Res.string.date),
            beginHeading = stringResource(Res.string.startDate),
            endHeading = stringResource(Res.string.endDate),
            filterText = filterText,
        )
        countryCode.ifNotNullOrEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.iso31661),
                text = it,
                filterText = filterText,
            )
            TextWithHeading(
                heading = stringResource(Res.string.regionalIndicatorSymbol),
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
