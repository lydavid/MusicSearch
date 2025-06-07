package ly.david.musicsearch.shared

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAppBottomNavigationBar() {
    PreviewTheme {
        Surface {
            AppBottomNavigationBar(
                currentTopLevelScreen = AppNavigationItem.Search.screen,
            )
        }
    }
}
