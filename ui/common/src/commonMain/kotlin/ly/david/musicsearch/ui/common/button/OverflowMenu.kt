package ly.david.musicsearch.ui.common.button

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.MoreVert
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.moreActions
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenu(
    overflowDropdownMenuItems: (@Composable OverflowMenuScope.() -> Unit)? = null,
    contentDescription: String = stringResource(Res.string.moreActions),
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    val scope = remember {
        object : OverflowMenuScope {
            override fun closeMenu() {
                showMenu = false
            }
        }
    }

    if (overflowDropdownMenuItems != null) {
        Box {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(
                    imageVector = CustomIcons.MoreVert,
                    contentDescription = contentDescription,
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                content = {
                    // We lose out on the ability to control these items within a Column,
                    // but now each item can close itself.
                    Column {
                        overflowDropdownMenuItems.invoke(scope)
                    }
                },
            )
        }
    }
}
