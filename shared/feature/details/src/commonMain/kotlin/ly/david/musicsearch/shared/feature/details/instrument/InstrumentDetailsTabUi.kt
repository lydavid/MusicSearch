package ly.david.musicsearch.shared.feature.details.instrument

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.HighlightableText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.description
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun InstrumentDetailsTabUi(
    instrument: InstrumentDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    DetailsTabUi(
        detailsModel = instrument,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = {
            type.ifNotEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.type),
                    text = it,
                    filterText = filterText,
                )
            }

            description.ifNotEmpty {
                val show = description.contains(filterText, ignoreCase = true)
                if (show) {
                    ListSeparatorHeader(stringResource(Res.string.description))
                    SelectionContainer {
                        HighlightableText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 4.dp,
                                ),
                            text = AnnotatedString(it),
                            highlightedText = filterText,
                        )
                    }
                }
            }
        },
        onCollapseExpandAliases = onCollapseExpandAliases,
    )
}
