package ly.david.musicsearch.ui.common.clipboard

import androidx.compose.ui.platform.Clipboard

expect suspend fun Clipboard.paste(): String
