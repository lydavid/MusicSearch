package ly.david.mbjc.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import ly.david.mbjc.BuildConfig

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
