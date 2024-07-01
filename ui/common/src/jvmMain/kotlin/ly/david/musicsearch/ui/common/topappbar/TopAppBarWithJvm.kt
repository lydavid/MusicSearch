package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

/**
 * [ScrollableTopAppBar] with filtering.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun TopAppBarWithFilter(
    modifier: androidx.compose.ui.Modifier,
    onBack: () -> Unit,
    showBackButton: Boolean,
    entity: ly.david.musicsearch.core.models.network.MusicBrainzEntity?,
    title: String,
    subtitle: String,
    scrollBehavior: androidx.compose.material3.TopAppBarScrollBehavior?,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    showFilterIcon: Boolean,
    filterText: String,
    onFilterTextChange: (String) -> Unit,
    additionalActions: @Composable () -> Unit,
    additionalBar: @Composable () -> Unit,
) {
    var isFilterMode by rememberSaveable { mutableStateOf(false) }

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
