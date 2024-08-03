package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

@Composable
fun rememberTopAppBarFilterState(
    initialFilterText: String = "",
    initialIsFilterMode: Boolean = false,
): TopAppBarFilterState {
    return rememberSaveable(saver = TopAppBarFilterState.Saver) {
        TopAppBarFilterState(
            initialFilterText = initialFilterText,
            initialIsFilterMode = initialIsFilterMode,
        )
    }
}

@Stable
class TopAppBarFilterState(
    initialFilterText: String = "",
    initialIsFilterMode: Boolean = false,
) {
    var filterText by mutableStateOf(initialFilterText)
        private set

    var isFilterMode by mutableStateOf(initialIsFilterMode)
        private set

    fun updateFilterText(newText: String) {
        filterText = newText
    }

    fun toggleFilterMode(enable: Boolean) {
        isFilterMode = enable
    }

    fun clear() {
        updateFilterText("")
    }

    fun dismiss() {
        toggleFilterMode(false)
        clear()
    }

    companion object {
        val Saver = listSaver<TopAppBarFilterState, Any>(
            save = { listOf(it.filterText, it.isFilterMode) },
            restore = {
                TopAppBarFilterState(
                    initialFilterText = it[0] as String,
                    initialIsFilterMode = it[1] as Boolean,
                )
            },
        )
    }
}
