package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberTopAppBarEditState(
    initialIsEditMode: Boolean = false,
): TopAppBarEditState {
    return rememberSaveable(saver = TopAppBarEditState.Saver) {
        TopAppBarEditState(
            showEditIcon = true,
            initialIsEditMode = initialIsEditMode,
        )
    }
}

@Stable
class TopAppBarEditState(
    val showEditIcon: Boolean = false,
    initialIsEditMode: Boolean = false,
) {
    var isEditMode by mutableStateOf(initialIsEditMode)
        private set

    fun toggleEditMode() {
        isEditMode = !isEditMode
    }

    fun toggleEditMode(enable: Boolean) {
        isEditMode = enable
    }

    fun dismiss() {
        toggleEditMode(false)
    }

    companion object {
        val Saver = listSaver<TopAppBarEditState, Any>(
            save = { listOf(it.showEditIcon, it.isEditMode) },
            restore = {
                TopAppBarEditState(
                    showEditIcon = it[0] as Boolean,
                    initialIsEditMode = it[1] as Boolean,
                )
            },
        )
    }
}
