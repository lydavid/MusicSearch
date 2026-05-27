package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.details.asEntity
import ly.david.musicsearch.shared.feature.details.utils.CollapsibleSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.LastListenedListItem
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.work.getDisplayLanguage
import ly.david.musicsearch.ui.common.work.getDisplayString
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
    snackbarHostState: SnackbarHostState,
) {
    val eventSink = detailsTabUiState.eventSink

    DetailsTabUi(
        detailsModel = work,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        snackbarHostState = snackbarHostState,
        entityInfoSection = {
            type?.getDisplayString()?.let {
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
                filterText = filterText,
                now = detailsTabUiState.now,
                collapsed = detailsTabUiState.isSectionCollapsed.contains(CollapsibleSection.Listens),
                onCollapseExpand = {
                    eventSink(DetailsTabUiEvent.ToggleCollapseExpandSection(CollapsibleSection.Listens))
                },
                onGoToListenAtEpochSeconds = { seconds ->
                    eventSink(
                        DetailsTabUiEvent.GoToScreen(
                            screen = ListensScreen(
                                dateTimeEpochSeconds = seconds,
                            ),
                        ),
                    )
                },
                onSeeAllListensClick = {
                    eventSink(
                        DetailsTabUiEvent.GoToScreen(
                            screen = ListensScreen(
                                entityFacet = work.asEntity(),
                            ),
                        ),
                    )
                },
            )
        },
    )
}

private fun LazyListScope.listenSection(
    work: WorkDetailsModel,
    filterText: String,
    now: Instant,
    collapsed: Boolean,
    onCollapseExpand: () -> Unit,
    onGoToListenAtEpochSeconds: (listenMs: Long) -> Unit,
    onSeeAllListensClick: () -> Unit,
) {
    if (work.listenCount != null) {
        stickyHeader {
            CollapsibleListSeparatorHeader(
                text = stringResource(Res.string.listens),
                collapsed = collapsed,
                onClick = onCollapseExpand,
                additionalContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = work.listenCount.toString(),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                },
            )
        }
        if (!collapsed) {
            items(work.latestListensTimestampsMs) {
                LastListenedListItem(
                    lastListenedMs = it,
                    now = now,
                    filterText = filterText,
                    onGoToListenAtEpochSeconds = onGoToListenAtEpochSeconds,
                )
            }
            item {
                ClickableItem(
                    title = stringResource(Res.string.seeAllListens),
                    filterText = filterText,
                    endIcon = CustomIcons.ChevronRight,
                    onClick = onSeeAllListensClick,
                )
            }
        }
    }
}
