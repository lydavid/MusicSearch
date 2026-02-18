package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.OpenInNew
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.openInBrowser
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.OpenInBrowserMenuItem(
    url: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current

    DropdownMenuItem(
        text = { Text(stringResource(Res.string.openInBrowser)) },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.OpenInNew,
                contentDescription = null,
            )
        },
        onClick = {
            uriHandler.openUri(url)
            closeMenu()
        },
        modifier = modifier,
    )
}
