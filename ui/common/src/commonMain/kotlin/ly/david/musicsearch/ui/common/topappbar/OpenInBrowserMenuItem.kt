package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
fun OverflowMenuScope.OpenInBrowserMenuItem(
    url: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val uriHandler = LocalUriHandler.current

    DropdownMenuItem(
        text = { Text(strings.openInBrowser) },
        onClick = {
            uriHandler.openUri(url)
            closeMenu()
        },
        modifier = modifier,
    )
}
