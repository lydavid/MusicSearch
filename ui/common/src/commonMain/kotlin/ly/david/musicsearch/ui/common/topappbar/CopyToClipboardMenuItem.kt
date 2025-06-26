package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import kotlinx.coroutines.launch
import ly.david.musicsearch.ui.common.clipboard.clipEntryWith
import ly.david.musicsearch.ui.common.icons.ContentCopy
import ly.david.musicsearch.ui.common.icons.CustomIcons

@Composable
fun OverflowMenuScope.CopyToClipboardMenuItem(
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    DropdownMenuItem(
        text = { Text("Copy ID to clipboard") },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.ContentCopy,
                contentDescription = null,
            )
        },
        onClick = {
            coroutineScope.launch {
                clipboard.setClipEntry(clipEntryWith(entityId))
                closeMenu()
            }
        },
        modifier = modifier,
    )
}
