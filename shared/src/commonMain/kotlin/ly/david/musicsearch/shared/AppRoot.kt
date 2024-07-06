package ly.david.musicsearch.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.ui.image.InitializeImageLoader

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppRoot(
    circuit: Circuit,
    initialScreens: ImmutableList<Screen>,
    modifier: Modifier = Modifier,
) {
    InitializeImageLoader()

    CircuitCompositionLocals(circuit) {
        ContentWithOverlays {
            val backStack = rememberSaveableBackStack(
                initialScreens = initialScreens,
            )
            val navigator = rememberCircuitNavigator(
                backStack = backStack,
                onRootPop = {},
            )

            val windowSizeClass = calculateWindowSizeClass()

            Scaffold(
                modifier = modifier,
                contentWindowInsets = WindowInsets(0),
                bottomBar = {
                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                        // TODO: This seems to add a blank space of equal height to iOS's screen
                        AppBottomNavigationBar(
                            currentTopLevelScreen = backStack.last().screen,
                            navigateToTopLevelScreen = { screen ->
                                navigator.resetRoot(screen)
                            },
                        )
                    }
                },
            ) { innerPadding ->

                Row {
                    if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                        AppNavigationRail(
                            currentTopLevelScreen = backStack.last().screen,
                            navigateToTopLevelScreen = { screen ->
                                navigator.resetRoot(screen)
                            },
                            modifier = Modifier.padding(innerPadding),
                        )
                    }

                    NavigableCircuitContent(
                        navigator = navigator,
                        backStack = backStack,
                        modifier = Modifier.padding(innerPadding),
                        decoration = GestureNavigationDecoration(
                            onBackInvoked = navigator::pop,
                        ),
                    )
                }
            }
        }
    }
}
