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
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.core.theme.TextStyles

@Composable
internal fun EventDetailsTabUi(
    event: EventDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    val entityInfoSection: @Composable EventDetailsModel.() -> Unit = {
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
        time?.ifNotNullOrEmpty {
            TextWithHeading(
                heading = strings.time,
                text = it,
                filterText = filterText,
            )
        }
        if (cancelled == true) {
            SelectionContainer {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = "(${strings.cancelled})",
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
    )
}
