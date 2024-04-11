package ly.david.musicsearch.shared

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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

@Composable
fun AppRoot(
    circuit: Circuit,
    initialScreens: ImmutableList<Screen>,
    modifier: Modifier = Modifier,
) {
    CircuitCompositionLocals(circuit) {
        ContentWithOverlays {
            val backStack = rememberSaveableBackStack(
                initialScreens = initialScreens,
            )
            val navigator = rememberCircuitNavigator(
                backStack = backStack,
                onRootPop = {},
            )

            Scaffold(
                modifier = modifier,
                bottomBar = {
                    // TODO: This seems to add a blank space of equal height to iOS's screen
                    BottomNavigationBar(
                        currentTopLevelScreen = backStack.last().screen,
                        navigateToTopLevelScreen = { screen ->
                            navigator.resetRoot(screen)
                        },
                    )
                },
            ) { innerPadding ->

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
