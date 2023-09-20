package ly.david.ui.common.paging

import app.cash.paging.PagingSource
import ly.david.data.domain.listitem.ListItemModel

interface StoreEntityUseCase {
    suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int?
    suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int
    suspend fun deleteLinkedEntitiesByEntity(entityId: String)
}

interface BrowseEntityUseCase<RM : Any, LI : ListItemModel> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String): PagingSource<Int, RM>
    fun transformRoomToListItemModel(roomModel: RM): LI
    fun postFilter(listItemModel: LI): Boolean {
        return true
    }
}

interface BrowseSortableEntityUseCase<RM : Any> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String, sorted: Boolean): PagingSource<Int, RM>
}
