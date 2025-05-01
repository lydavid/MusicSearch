package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryWith(text: String): ClipEntry {
    return ClipEntry.withPlainText(text)
}
