package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

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
    entity: MusicBrainzEntity?,
    subtitle: String,
    scrollBehavior: TopAppBarScrollBehavior?,
    overflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    subtitleDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit)?,
    topAppBarFilterState: TopAppBarFilterState,
    selectionState: SelectionState,
    additionalActions: @Composable (() -> Unit),
    additionalBar: @Composable (() -> Unit),
    onSelectAllToggle: () -> Unit,
) {
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
