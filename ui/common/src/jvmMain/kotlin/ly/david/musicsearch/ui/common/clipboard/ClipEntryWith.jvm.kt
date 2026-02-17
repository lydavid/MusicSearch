package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import java.awt.datatransfer.StringSelection

@OptIn(ExperimentalComposeUiApi::class)
actual fun clipEntryWith(text: String): ClipEntry {
    return ClipEntry(StringSelection(text))
}
