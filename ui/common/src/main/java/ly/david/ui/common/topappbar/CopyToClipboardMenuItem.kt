package ly.david.ui.common.topappbar

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import ly.david.ui.common.BuildConfig

@Composable
fun OverflowMenuScope.CopyToClipboardMenuItem(
    resourceId: String,
    modifier: Modifier = Modifier
) {
    if (!BuildConfig.DEBUG) return

    val clipboardManager = LocalClipboardManager.current

    DropdownMenuItem(
        text = { Text("Copy to clipboard") },
        onClick = {
            clipboardManager.setText(AnnotatedString(resourceId))
            closeMenu()
        },
        modifier = modifier
    )
}
