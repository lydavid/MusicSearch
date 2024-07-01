package ly.david.ui.common.topappbar

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

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

    showFilterIcon: Boolean,
    filterText: String,
    onFilterTextChange: (String) -> Unit,
    additionalActions: @Composable () -> Unit,

    additionalBar: @Composable () -> Unit,
) {
    var isFilterMode by rememberSaveable { mutableStateOf(false) }

    if (isFilterMode) {
        BackHandler {
            isFilterMode = false
            onFilterTextChange("")
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
        showFilterIcon = showFilterIcon,
        filterText = filterText,
        onFilterTextChange = onFilterTextChange,
        isFilterMode = isFilterMode,
        onFilterModeChange = { isFilterMode = it },
        additionalActions = additionalActions,
        additionalBar = additionalBar,
    )
}

// region Previews
@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewTopAppBarWithFilter() {
    PreviewTheme {
        TopAppBarWithFilterInternal(title = "Title")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewTopAppBarWithFilterFilterMode() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            isFilterMode = true,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewTopAppBarWithFilterNoFilter() {
    PreviewTheme {
        TopAppBarWithFilterInternal(
            title = "Title",
            showFilterIcon = false,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun PreviewTopAppBarWithFilterWithTabs() {
    PreviewTheme {
        var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

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
// endregion
