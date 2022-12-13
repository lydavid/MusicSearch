package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingSource
import ly.david.data.persistence.RoomModel

interface StoreResourceUseCase<T: RoomModel> {
    suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int?
    suspend fun getLocalLinkedResourcesCountByResource(resourceId: String): Int
    suspend fun deleteLinkedResourcesByResource(resourceId: String)
}

interface BrowseResourceUseCase<T: RoomModel> : StoreResourceUseCase<T> {
    fun getLinkedResourcesPagingSource(resourceId: String, query: String): PagingSource<Int, T>
}

interface BrowseSortableResourceUseCase<T: RoomModel> : StoreResourceUseCase<T> {
    fun getLinkedResourcesPagingSource(resourceId: String, query: String, sorted: Boolean): PagingSource<Int, T>
}
