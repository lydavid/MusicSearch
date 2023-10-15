package ly.david.ui.common.paging

import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.models.listitem.ListItemModel

interface SortablePagedList<LI : ListItemModel> : IPagedList<LI> {
    data class ViewModelState(
        val entityId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val sorted: Boolean = false,
    )

    val sorted: MutableStateFlow<Boolean>

    fun updateSorted(sorted: Boolean) {
        this.sorted.value = sorted
    }
}
