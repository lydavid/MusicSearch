package ly.david.musicsearch.shared

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.sharedelements.SharedElementTransitionLayout
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import ly.david.musicsearch.ui.common.image.InitializeImageLoader

@OptIn(
    ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun AppRoot(
    circuit: Circuit,
    backStack: SaveableBackStack,
    navigator: Navigator,
    modifier: Modifier = Modifier,
) {
    InitializeImageLoader()

    CircuitCompositionLocals(circuit) {
        SharedElementTransitionLayout {
            ContentWithOverlays {
                ly.david.musicsearch.ui.common.theme.ProvideStrings {
                    val windowSizeClass = calculateWindowSizeClass()
                    val isCompact = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

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
                                    onBackInvoked = navigator::pop,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}
