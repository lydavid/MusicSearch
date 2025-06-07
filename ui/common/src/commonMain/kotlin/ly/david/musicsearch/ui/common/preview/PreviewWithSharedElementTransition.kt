package ly.david.musicsearch.ui.common.preview

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.slack.circuit.sharedelements.PreviewSharedElementTransitionLayout
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Suppress("ModifierMissing")
@Composable
fun PreviewWithSharedElementTransition(content: @Composable () -> Unit) {
    PreviewTheme {
        PreviewSharedElementTransitionLayout {
            Surface {
                content()
            }
        }
    }
}

