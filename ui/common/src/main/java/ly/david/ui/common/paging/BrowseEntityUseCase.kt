package ly.david.ui.common.paging

import androidx.paging.PagingSource
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.room.RoomModel

interface StoreEntityUseCase {
    suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int?
    suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int
    suspend fun deleteLinkedEntitiesByEntity(entityId: String)
}

interface BrowseEntityUseCase<RM : RoomModel, LI : ListItemModel> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String): PagingSource<Int, RM>
    fun transformRoomToListItemModel(roomModel: RM): LI
    fun postFilter(listItemModel: LI): Boolean {
        return true
    }
}

interface BrowseSortableEntityUseCase<RM : RoomModel> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String, sorted: Boolean): PagingSource<Int, RM>
}
