package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Refresh
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.refresh
import musicsearch.ui.common.generated.resources.refreshXTab
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.RefreshMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    show: Boolean = true,
    tab: Tab? = null,
) {
    val tabTitle = tab?.getTitle()
    val title = if (tabTitle == null) {
        stringResource(Res.string.refresh)
    } else {
        // TODO: We need to have strings for title case and sentence case
        stringResource(Res.string.refreshXTab, tabTitle.lowercase())
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
