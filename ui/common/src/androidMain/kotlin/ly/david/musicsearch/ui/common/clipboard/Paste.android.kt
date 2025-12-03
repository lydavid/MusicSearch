package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.platform.Clipboard

private const val FIRST_ITEM_INDEX = 0

actual suspend fun Clipboard.paste(): String {
    return this.nativeClipboard.primaryClip?.getItemAt(FIRST_ITEM_INDEX)?.text?.toString().orEmpty()
}
