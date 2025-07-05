package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentSet

@Composable
fun rememberSelectionState(
    totalCount: Int = 0,
): SelectionState {
    val selectionState = rememberSaveable(saver = SelectionState.Saver) {
        SelectionState()
    }
    LaunchedEffect(totalCount) {
        selectionState.totalCount = totalCount
    }
    return selectionState
}

enum class SelectAllState {
    None,
    Some,
    All,
}

@Stable
class SelectionState {
    /**
     * The number of items that could be displayed in this screen.
     * Not all may have been loaded.
     */
    var totalCount by mutableIntStateOf(0)

    /**
     * The number of items that could be displayed in this screen and have been loaded.
     */
    private var loadedCount by mutableIntStateOf(0)

    var selectedIds by mutableStateOf(persistentSetOf<String>())
        private set

    val isEditMode: Boolean
        get() = selectedIds.isNotEmpty()

    val selectedCount: Int
        get() = selectedIds.size

    val title: String
        get() = if (selectedIds.isNotEmpty()) {
            "Selected $selectedCount / $totalCount"
        } else {
            ""
        }

    val selectAllState: SelectAllState
        get() {
            return when {
                selectedIds.isEmpty() -> SelectAllState.None
                selectedIds.size == totalCount -> SelectAllState.All
                else -> SelectAllState.Some
            }
        }

    /**
     * For simplicity, we have chosen not to automatically update this text when new items are loaded.
     * So, clicking "Deselect all" after loading new items will actually select all.
     * Afterwards, it would be accurate again.
     */
    val checkboxText: String
        get() {
            return if (selectedIds.size == loadedCount) {
                "Deselect all"
            } else {
                "Select all"
            }
        }

    fun toggleSelection(
        id: String,
        totalLoadedCount: Int,
    ) {
        selectedIds = if (selectedIds.contains(id)) {
            selectedIds.minus(id)
        } else {
            selectedIds.plus(id)
        }.toPersistentSet()
        this.loadedCount = totalLoadedCount
    }

    fun toggleSelectAll(ids: List<String>) {
        // less-than-or-equals handles the restoration case where the passed on ids is a subset of the selected ids
        // because paging did not prepend load everything that came before the current point
        selectedIds = if (ids.size <= selectedIds.size) {
            persistentSetOf()
        } else {
            selectedIds.plus(ids).toPersistentSet()
        }
        loadedCount = ids.size
    }

    fun clearSelection() {
        selectedIds = persistentSetOf()
    }

    companion object {
        val Saver = listSaver<SelectionState, Any>(
            save = {
                listOf(
                    it.totalCount,
                    it.loadedCount,
                    it.selectedIds.toSet(),
                )
            },
            restore = {
                SelectionState().apply {
                    totalCount = it[0] as Int
                    loadedCount = it[1] as Int
                    selectedIds = (it[2] as Set<String>).toPersistentSet()
                }
            },
        )
    }
}
