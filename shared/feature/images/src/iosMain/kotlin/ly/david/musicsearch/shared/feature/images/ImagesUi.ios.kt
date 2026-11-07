package ly.david.musicsearch.shared.feature.images

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
internal actual fun ImagesUi(
    state: ImagesUiState,
    modifier: Modifier,
) {
    val windowSizeClass = calculateWindowSizeClass()
    ImagesUi(
        state = state,
        isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact,
        modifier = modifier,
    )
}
