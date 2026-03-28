package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import ly.david.musicsearch.shared.domain.listitem.SelectableId

@Composable
fun rememberSelectionState(
    totalCount: Int = 0,
): SelectionState {
    val selectionState = rememberRetained {
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

    var selectedItems: PersistentList<SelectableId> by mutableStateOf(persistentListOf())
        private set

    val selectedIds: ImmutableList<String>
        get() = selectedItems.map { it.id }.toPersistentList()

    val selectedRecordingIds: ImmutableList<String>
        get() = selectedItems.mapNotNull { it.recordingId }.toPersistentList()

    val isEditMode: Boolean
        get() = selectedItems.isNotEmpty()

    val selectedCount: Int
        get() = selectedItems.size

    val title: String
        get() = if (selectedItems.isNotEmpty()) {
            "Selected $selectedCount / $totalCount"
        } else {
            ""
        }

    val selectAllState: SelectAllState
        get() {
            return when {
                selectedItems.isEmpty() -> SelectAllState.None
                selectedItems.size == totalCount -> SelectAllState.All
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
            return if (selectedItems.size == loadedCount) {
                "Deselect all"
            } else {
                "Select all"
            }
        }

    fun toggleSelection(
        item: SelectableId,
        totalLoadedCount: Int,
    ) {
        selectedItems = if (selectedItems.any { it.id == item.id }) {
            selectedItems.removeAll { it.id == item.id }
        } else {
            selectedItems.add(item)
        }
        loadedCount = totalLoadedCount
    }

    fun toggleSelectAll(items: List<SelectableId>) {
        // less-than-or-equals handles the restoration case where the passed on ids is a subset of the selected ids
        // because paging did not prepend load everything that came before the current point
        selectedItems = if (items.size <= selectedItems.size) {
            persistentListOf()
        } else {
            val existingIds = selectedItems.map { it.id }.toHashSet()
            selectedItems.addAll(items.filter { it.id !in existingIds })
        }
        loadedCount = items.size
    }

    fun clearSelection() {
        selectedItems = persistentListOf()
    }
}
