package ly.david.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> rememberFlowWithLifecycleStarted(
    flow: Flow<T>,
    lifecycleOwner: Lifecycle = LocalLifecycleOwner.current.lifecycle
): Flow<T> = remember(flow, lifecycleOwner) {
    flow.flowWithLifecycle(
        lifecycle = lifecycleOwner,
        minActiveState = Lifecycle.State.STARTED
    )
}
