package ly.david.musicsearch.ui.common.clipboard

import android.content.ClipData
import androidx.compose.ui.platform.ClipEntry

actual fun clipEntryWith(text: String): ClipEntry {
    return ClipEntry(clipData = ClipData.newPlainText("", text))
}
