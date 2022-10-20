package ly.david.mbjc.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.BuildConfig
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.lookupInBrowser

internal interface OverflowMenuScope {
    fun closeMenu()
}

@Composable
internal fun OverflowMenuScope.OpenInBrowserMenuItem(resource: MusicBrainzResource, resourceId: String) {
    val context = LocalContext.current

    DropdownMenuItem(
        text = { Text(stringResource(id = R.string.open_in_browser)) },
        onClick = {
            context.lookupInBrowser(resource, resourceId)
            closeMenu()
        }
    )
}

@Composable
internal fun OverflowMenuScope.CopyToClipboardMenuItem(resourceId: String) {
    if (!BuildConfig.DEBUG) return

    val clipboardManager = LocalClipboardManager.current

    DropdownMenuItem(
        text = { Text("Copy to clipboard") },
        onClick = {
            clipboardManager.setText(AnnotatedString(resourceId))
            closeMenu()
        }
    )
}
