package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingSource
import ly.david.data.persistence.RoomModel

interface BrowseResourceUseCase<T: RoomModel> {
    suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int?
    suspend fun getLocalLinkedResourcesCountByResource(resourceId: String): Int
    suspend fun deleteLinkedResourcesByResource(resourceId: String)
    fun getLinkedResourcesPagingSource(resourceId: String, query: String): PagingSource<Int, T>
}
