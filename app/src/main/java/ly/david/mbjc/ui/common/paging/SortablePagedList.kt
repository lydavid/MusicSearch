package ly.david.mbjc.ui.common.paging

import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.domain.ListItemModel

internal interface SortablePagedList<LI : ListItemModel> : IPagedList<LI> {
    data class ViewModelState(
        val resourceId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val sorted: Boolean = false
    )

    val sorted: MutableStateFlow<Boolean>

    fun updateSorted(sorted: Boolean) {
        this.sorted.value = sorted
    }
}
