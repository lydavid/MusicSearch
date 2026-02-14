package ly.david.musicsearch.shared.feature.details.label

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.LabelDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.area.AreaSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.defunct
import musicsearch.ui.common.generated.resources.founded
import musicsearch.ui.common.generated.resources.ipi
import musicsearch.ui.common.generated.resources.isni
import musicsearch.ui.common.generated.resources.labelCode
import musicsearch.ui.common.generated.resources.lc
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LabelDetailsTabUi(
    label: LabelDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
) {
    DetailsTabUi(
        detailsModel = label,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        onCollapseExpandAliases = onCollapseExpandAliases,
        entityInfoSection = {
            type.ifNotEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.type),
                    text = it,
                    filterText = filterText,
                )
            }
            labelCode?.ifNotNull {
                TextWithHeading(
                    heading = stringResource(Res.string.labelCode),
                    text = stringResource(Res.string.lc, it),
                    filterText = filterText,
                )
            }

            ipis.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.ipi),
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }

            isnis.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.isni),
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }

            lifeSpan.run {
                begin.ifNotEmpty {
                    TextWithHeading(
                        heading = stringResource(Res.string.founded),
                        text = it,
                        filterText = filterText,
                    )
                }
                end.ifNotEmpty {
                    TextWithHeading(
                        heading = stringResource(Res.string.defunct),
                        text = it,
                        filterText = filterText,
                    )
                }
            }

            AreaSection(
                areaListItemModel = area,
                filterText = filterText,
                onItemClick = onItemClick,
            )
        },
    )
}
