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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.preview.PreviewTheme

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TopAppBarWithFilter(
    annotatedString: AnnotatedString,
    modifier: Modifier,
    onBack: () -> Unit,
    showBackButton: Boolean,
    onSelectAllToggle: () -> Unit,
    entity: MusicBrainzEntityType?,
    subtitle: String,
    scrollBehavior: TopAppBarScrollBehavior?,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    topAppBarFilterState: TopAppBarFilterState,
    selectionState: SelectionState,
    additionalActions: @Composable (() -> Unit),
    additionalBar: @Composable (() -> Unit),
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
        annotatedString = annotatedString,
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
        TopAppBarWithFilterInternal(
            annotatedString = ArtistDetailsModel(
                id = "8b089567-8879-4d0e-bf24-6f8352b63a16",
                name = "ナノ",
                disambiguation = "Japanese-American pop/rock singer",
                aliases = persistentListOf(
                    BasicAlias(
                        name = "NANO",
                        locale = "en",
                        isPrimary = true,
                    ),
                ),
            ).getAnnotatedName(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterFilterMode() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            annotatedString = AnnotatedString(text = "Title"),
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
            annotatedString = AnnotatedString(text = "Title"),
            topAppBarFilterState = TopAppBarFilterState(
                initialFilterText = "Initial text",
                initialIsFilterMode = true,
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
            annotatedString = AnnotatedString(text = "Title"),
            topAppBarFilterState = TopAppBarFilterState(
                initialShow = false,
            ),
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
            annotatedString = AnnotatedString(text = "Title"),
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
            annotatedString = AnnotatedString(text = "Title"),
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
internal fun PreviewTopAppBarWithFilterWithSelectedOne() {
    PreviewTheme {
        val selectionState = rememberSelectionState(totalCount = 300)
        selectionState.toggleSelection(
            id = "1",
            totalLoadedCount = 200,
        )
        TopAppBarWithFilterInternal(
            annotatedString = AnnotatedString(text = "Title"),
            selectionState = selectionState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithSelectedAll() {
    PreviewTheme {
        val selectionState = rememberSelectionState(totalCount = 200)
        selectionState.toggleSelectAll(
            ids = (1..200).map { it.toString() },
        )
        TopAppBarWithFilterInternal(
            annotatedString = AnnotatedString(text = "Title"),
            selectionState = selectionState,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
internal fun PreviewTopAppBarWithFilterWithSelectedAllExceptOne() {
    PreviewTheme {
        val selectionState = rememberSelectionState(totalCount = 200)
        selectionState.toggleSelectAll(
            ids = (1..199).map { it.toString() },
        )
        TopAppBarWithFilterInternal(
            annotatedString = AnnotatedString(text = "Title"),
            selectionState = selectionState,
        )
    }
}
// endregion
