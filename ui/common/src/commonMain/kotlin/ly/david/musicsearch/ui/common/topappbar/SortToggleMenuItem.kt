package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Reorder
import ly.david.musicsearch.ui.common.icons.Sort
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
fun OverflowMenuScope.SortToggleMenuItem(
    sorted: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    ToggleMenuItem(
        toggleOnText = strings.sort,
        toggleOffText = strings.unsort,
        toggled = sorted,
        onToggle = onToggle,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = if (sorted) CustomIcons.Reorder else CustomIcons.Sort,
                contentDescription = null,
            )
        },
    )
}
