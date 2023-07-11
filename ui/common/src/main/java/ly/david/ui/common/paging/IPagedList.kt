package ly.david.ui.common.paging

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.data.domain.listitem.ListItemModel

interface IPagedList<LI : ListItemModel> {
    data class ViewModelState(
        val entityId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
    )

    val entityId: MutableStateFlow<String>
    val query: MutableStateFlow<String>
    val isRemote: MutableStateFlow<Boolean>

    fun loadPagedEntities(entityId: String) {
        this.entityId.value = entityId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun setRemote(isRemote: Boolean) {
        this.isRemote.value = isRemote
    }

    val pagedEntities: Flow<PagingData<LI>>
}
