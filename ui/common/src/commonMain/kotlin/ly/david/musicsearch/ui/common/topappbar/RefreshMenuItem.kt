package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
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
        val tabName = if (Locale.current.language == "de") {
            tabTitle
        } else {
            tabTitle.lowercase()
        }
        stringResource(Res.string.refreshXTab, tabName)
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
