package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.domain.ListItemModel

internal interface IPagedList<LI : ListItemModel> {
    data class ViewModelState(
        val resourceId: String = "",
        val query: String = "",
        val isRemote: Boolean = true
    )

    val resourceId: MutableStateFlow<String>
    val query: MutableStateFlow<String>
    val isRemote: MutableStateFlow<Boolean>

    fun loadPagedResources(resourceId: String) {
        this.resourceId.value = resourceId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun setRemote(isRemote: Boolean) {
        this.isRemote.value = isRemote
    }

    val pagedResources: Flow<PagingData<LI>>
}

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
