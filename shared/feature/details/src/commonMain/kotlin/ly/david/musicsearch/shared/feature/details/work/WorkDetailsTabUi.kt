package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.work.getDisplayLanguage

@Composable
internal fun WorkDetailsTabUi(
    work: WorkDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    val strings = LocalStrings.current

    DetailsTabUi(
        detailsModel = work,
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
            languages.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.language,
                    text = it.mapNotNull { language ->
                        language.getDisplayLanguage(strings)
                    }.joinToString(", "),
                    filterText = filterText,
                )
            }
            iswcs.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = strings.iswc,
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            work.run {
                if (attributes.isNotEmpty()) {
                    item {
                        ListSeparatorHeader(strings.attributesHeader(strings.work))
                    }
                    items(work.attributes) { attribute ->
                        TextWithHeading(
                            heading = attribute.type,
                            text = attribute.value,
                            filterText = filterText,
                        )
                    }
                }
            }
        },
    )
}
