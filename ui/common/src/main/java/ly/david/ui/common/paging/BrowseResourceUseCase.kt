package ly.david.ui.common.paging

import androidx.paging.PagingSource
import ly.david.data.domain.ListItemModel
import ly.david.data.persistence.RoomModel

interface StoreResourceUseCase {
    suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int?
    suspend fun getLocalLinkedResourcesCountByResource(resourceId: String): Int
    suspend fun deleteLinkedResourcesByResource(resourceId: String)
}

interface BrowseResourceUseCase<RM: RoomModel, LI: ListItemModel> : StoreResourceUseCase {
    fun getLinkedResourcesPagingSource(resourceId: String, query: String): PagingSource<Int, RM>
    fun transformRoomToListItemModel(roomModel: RM): LI
    fun postFilter(listItemModel: LI): Boolean {
        return true
    }
}

interface BrowseSortableResourceUseCase<RM: RoomModel> : StoreResourceUseCase {
    fun getLinkedResourcesPagingSource(resourceId: String, query: String, sorted: Boolean): PagingSource<Int, RM>
}
