package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.feature.details.LastListenedListItem
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.attributesHeader
import musicsearch.ui.common.generated.resources.iswc
import musicsearch.ui.common.generated.resources.language
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.seeAllListens
import musicsearch.ui.common.generated.resources.type
import musicsearch.ui.common.generated.resources.work
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun WorkDetailsTabUi(
    work: WorkDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onSeeAllListensClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
) {
    DetailsTabUi(
        detailsModel = work,
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
            languages.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.language),
                    text = it.mapNotNull { language ->
                        language.getDisplayLanguage()
                    }.joinToString(", "),
                    filterText = filterText,
                )
            }
            iswcs.ifNotNullOrEmpty {
                TextWithHeading(
                    heading = stringResource(Res.string.iswc),
                    text = it.joinToString(", "),
                    filterText = filterText,
                )
            }
        },
        bringYourOwnLabelsSection = {
            if (work.attributes.isNotEmpty()) {
                item {
                    ListSeparatorHeader(
                        stringResource(
                            Res.string.attributesHeader,
                            stringResource(Res.string.work),
                        ),
                    )
                }
                items(work.attributes) { attribute ->
                    TextWithHeading(
                        heading = attribute.type,
                        text = attribute.value,
                        filterText = filterText,
                    )
                }
            }

            listenSection(
                work = work,
                now = detailsTabUiState.now,
                onSeeAllListensClick = onSeeAllListensClick,
            )
        },
    )
}

private fun LazyListScope.listenSection(
    work: WorkDetailsModel,
    now: Instant,
    onSeeAllListensClick: () -> Unit = {},
) {
    if (work.listenCount != null) {
        item {
            ListSeparatorHeader(stringResource(Res.string.listens))
        }
        item {
            ListItem(
                headlineContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = work.listenCount.toString(),
                    )
                },
            )
        }
        items(work.latestListensTimestampsMs) {
            LastListenedListItem(
                lastListenedMs = it,
                now = now,
            )
        }
        item {
            ClickableItem(
                title = stringResource(Res.string.seeAllListens),
                endIcon = CustomIcons.ChevronRight,
                onClick = onSeeAllListensClick,
            )
        }
    }
}
