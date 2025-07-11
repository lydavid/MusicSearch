package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Refresh
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
fun OverflowMenuScope.RefreshMenuItem(
    show: Boolean = true,
    tab: Tab? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    val tabTitle = tab?.getTitle(strings)
    val title = if (tabTitle == null) {
        strings.refresh
    } else {
        strings.refreshXTab(tabTitle)
    }

    if (show) {
        DropdownMenuItem(
            text = {
                Text(
                    text = title,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = CustomIcons.Refresh,
                    contentDescription = null,
                )
            },
            onClick = {
                onClick()
                closeMenu()
            },
            modifier = modifier,
        )
    }
}
