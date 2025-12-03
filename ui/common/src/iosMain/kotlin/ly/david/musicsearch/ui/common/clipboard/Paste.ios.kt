package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.Clipboard

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun Clipboard.paste(): String {
    return this.getClipEntry()?.getPlainText() ?: ""
}
