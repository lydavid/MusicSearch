package ly.david.musicsearch.shared

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.sharedelements.SharedElementTransitionLayout
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import com.slack.circuitx.gesturenavigation.GestureNavigationEventListener
import ly.david.musicsearch.ui.common.image.InitializeImageLoader

@OptIn(
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun AppRoot(
    backStack: SaveableBackStack,
    navigator: Navigator,
    isCompact: Boolean,
    modifier: Modifier = Modifier,
) {
    InitializeImageLoader()

    SharedElementTransitionLayout {
        ContentWithOverlays {
            Scaffold(
                modifier = modifier,
                contentWindowInsets = WindowInsets(0),
                bottomBar = {
                    if (isCompact) {
                        AppBottomNavigationBar(
                            currentTopLevelScreen = backStack.last().screen,
                            navigateToTopLevelScreen = { screen ->
                                navigator.resetRoot(screen)
                            },
                        )
                    }
                },
            ) { innerPadding ->

                Row(
                    modifier = Modifier
                        .padding(innerPadding),
                ) {
                    if (!isCompact) {
                        AppNavigationRail(
                            currentTopLevelScreen = backStack.last().screen,
                            navigateToTopLevelScreen = { screen ->
                                navigator.resetRoot(screen)
                            },
                        )
                    }

                    val contentModifier: Modifier = if (isCompact) {
                        Modifier
                    } else {
                        Modifier.navigationBarsPadding()
                    }
                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = contentModifier,
                        decoratorFactory = GestureNavigationDecorationFactory(
                            listener = GestureNavigationEventListener.NoOp,
                        ),
                    )
                }
            }
        }
    }
}
