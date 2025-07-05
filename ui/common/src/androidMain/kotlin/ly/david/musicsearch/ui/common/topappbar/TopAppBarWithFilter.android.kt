package ly.david.musicsearch.ui.common.topappbar

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.preview.PreviewTheme

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TopAppBarWithFilter(
    modifier: Modifier,
    onBack: () -> Unit,
    showBackButton: Boolean,
    entity: MusicBrainzEntity?,
    title: String,
    subtitle: String,
    scrollBehavior: TopAppBarScrollBehavior?,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    topAppBarFilterState: TopAppBarFilterState,
    selectionState: SelectionState,
    additionalActions: @Composable () -> Unit,
    additionalBar: @Composable () -> Unit,
    onSelectAllToggle: () -> Unit,
) {
    if (topAppBarFilterState.isFilterMode || selectionState.isEditMode) {
        BackHandler {
            if (topAppBarFilterState.isFilterMode) {
                topAppBarFilterState.dismiss()
            } else if (selectionState.isEditMode) {
                selectionState.clearSelection()
            }
        }
    }

    TopAppBarWithFilterInternal(
        modifier = modifier,
        onBack = onBack,
        showBackButton = showBackButton,
        entity = entity,
        title = title,
        subtitle = subtitle,
        scrollBehavior = scrollBehavior,
        overflowDropdownMenuItems = overflowDropdownMenuItems,
        subtitleDropdownMenuItems = subtitleDropdownMenuItems,
        topAppBarFilterState = topAppBarFilterState,
        selectionState = selectionState,
        additionalActions = additionalActions,
        additionalBar = additionalBar,
        onSelectAllToggle = onSelectAllToggle,
    )
}

// region Previews
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilter() {
    PreviewTheme {
        TopAppBarWithFilterInternal(title = "Title")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterFilterMode() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            topAppBarFilterState = TopAppBarFilterState(initialIsFilterMode = true),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterFilterModeWithText() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            topAppBarFilterState = TopAppBarFilterState(
                initialFilterText = "Initial text",
                initialIsFilterMode = true
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterNoFilter() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            topAppBarFilterState = TopAppBarFilterState(
                initialShow = false,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithTabs() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        TopAppBarWithFilterInternal(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            additionalBar = {
                TabsBar(
                    tabsTitle = listOf(
                        "Tab 1",
                        "Tab 2",
                        "Tab 3",
                    ),
                    selectedTabIndex = selectedTabIndex,
                    onSelectTabIndex = { selectedTabIndex = it },
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithTabsFilterMode() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        TopAppBarWithFilterInternal(
            title = "A title that is very long so that it will go off the screen and allow us to scroll.",
            topAppBarFilterState = TopAppBarFilterState(initialIsFilterMode = true),
            additionalBar = {
                TabsBar(
                    tabsTitle = listOf(
                        "Tab 1",
                        "Tab 2",
                        "Tab 3",
                    ),
                    selectedTabIndex = selectedTabIndex,
                    onSelectTabIndex = { selectedTabIndex = it },
                )
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithWithSelectionDeselectAll() {
    PreviewTheme {
        val selectionState = rememberSelectionState(totalCount = 300)
        selectionState.toggleSelection(
            id = "1",
            totalLoadedCount = 200,
        )
        TopAppBarWithFilterInternal(
            title = "Title",
            selectionState = selectionState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithSelectionSelectAll() {
    PreviewTheme {
        val selectionState = rememberSelectionState(totalCount = 200)
        selectionState.toggleSelectAll(
            ids = (1..200).map { it.toString() },
        )
        TopAppBarWithFilterInternal(
            title = "Title",
            selectionState = selectionState,
        )
    }
}
// endregion
