package ly.david.musicsearch.shared.feature.details.event

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cancelled
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.endDate
import musicsearch.ui.common.generated.resources.startDate
import musicsearch.ui.common.generated.resources.time
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EventDetailsTabUi(
    event: EventDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val entityInfoSection: @Composable EventDetailsModel.() -> Unit = {
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
        time.ifNotEmpty {
            TextWithHeading(
                heading = stringResource(Res.string.time),
                text = it,
                filterText = filterText,
            )
        }
        if (cancelled) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = "(${stringResource(Res.string.cancelled)})",
                    style = TextStyles.getCardBodyTextStyle(),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }

        // TODO: set list
        //  api for this seems like some kind markdown?
    }
    DetailsTabUi(
        detailsModel = event,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = entityInfoSection,
        onCollapseExpandAliases = onCollapseExpandAliases,
    )
}
