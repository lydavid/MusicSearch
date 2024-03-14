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

@Composable
fun AppRoot(
    circuit: Circuit,
    initialScreens: List<Screen>,
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
                bottomBar = {
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
                )
            }
        }
    }
}
