package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

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
    topAppBarFilterState: TopAppBarFilterState,
    topAppBarEditState: TopAppBarEditState,

    additionalActions: @Composable () -> Unit,
    additionalBar: @Composable () -> Unit,
) {
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
        topAppBarFilterState = topAppBarFilterState,
        topAppBarEditState = topAppBarEditState,
        additionalActions = additionalActions,
        additionalBar = additionalBar,
    )
}
