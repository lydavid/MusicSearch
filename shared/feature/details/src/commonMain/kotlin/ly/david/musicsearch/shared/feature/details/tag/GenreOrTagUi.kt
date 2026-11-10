package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Tag
import ly.david.musicsearch.ui.common.icons.TheaterComedy
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GenreOrTagUi(
    state: GenreOrTagUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val snackbarHostState = remember { SnackbarHostState() }

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = true,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            TopAppBarWithFilter(
                showBackButton = true,
                onBack = {
                    eventSink(EntitiesByTagUiEvent.NavigateUp)
                },
                imageVector = if (state.isGenre) CustomIcons.TheaterComedy else CustomIcons.Tag,
                title = state.name,
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
                overflowDropdownMenuItems = {
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                },
            )
        },
    ) { innerPadding, scrollBehavior ->

        ScreenWithPagingLoadingAndError(
            lazyPagingItems = state.lazyPagingItems,
            modifier = Modifier
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            lazyListState = state.lazyListState,
            keyed = false,
            isPullToRefreshEnabled = state.isPullToRefreshEnabled,
            itemContent = { item ->
                when (item) {
                    is TaggedEntityListItemModel -> {
                        ClickableItem(
                            title = item.entityName,
                            subtitle = item.disambiguation,
                            filterText = state.topAppBarFilterState.filterText,
                            startIcon = item.entityType.getIcon(),
                            onClick = {
                                eventSink(
                                    EntitiesByTagUiEvent.GoToEntity(
                                        entityType = item.entityType,
                                        id = item.id,
                                    ),
                                )
                            },
                        )
                    }

                    else -> {
                        // Do nothing.
                    }
                }
            },
        )
    }
}
