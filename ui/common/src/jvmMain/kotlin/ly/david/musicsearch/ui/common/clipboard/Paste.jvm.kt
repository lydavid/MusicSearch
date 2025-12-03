package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.awtClipboard
import java.awt.datatransfer.DataFlavor

@OptIn(ExperimentalComposeUiApi::class)
actual suspend fun Clipboard.paste(): String {
    val transferable = this.awtClipboard?.getContents(null)
    return if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        (transferable.getTransferData(DataFlavor.stringFlavor) as? String).orEmpty()
    } else {
        ""
    }
}
