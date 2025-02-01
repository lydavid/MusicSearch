package ly.david.musicsearch.ui.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun screenContainerSize(): IntSize {
    return LocalWindowInfo.current.containerSize
}
