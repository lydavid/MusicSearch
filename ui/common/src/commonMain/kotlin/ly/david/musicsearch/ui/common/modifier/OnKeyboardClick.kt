package ly.david.musicsearch.ui.common.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type

fun Modifier.onKeyboardClick(onKeyboardClick: () -> Unit): Modifier {
    return onKeyEvent {
        if (it.type == KeyEventType.KeyUp) {
            when (it.key) {
                Key.Spacebar, Key.Enter -> {
                    onKeyboardClick()
                    true
                }

                else -> false
            }
        } else {
            false
        }
    }
}
