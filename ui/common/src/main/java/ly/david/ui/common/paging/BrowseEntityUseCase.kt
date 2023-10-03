package ly.david.ui.common.paging

import app.cash.paging.PagingSource
import ly.david.musicsearch.domain.listitem.ListItemModel

interface StoreEntityUseCase {
    suspend fun browseLinkedEntitiesAndStore(entityId: String, nextOffset: Int): Int
    suspend fun getRemoteLinkedEntitiesCountByEntity(entityId: String): Int?
    suspend fun getLocalLinkedEntitiesCountByEntity(entityId: String): Int
    suspend fun deleteLinkedEntitiesByEntity(entityId: String)
}

interface BrowseEntityUseCase<DM : Any, LI : ListItemModel> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String): PagingSource<Int, DM>
    fun transformDatabaseToListItemModel(databaseModel: DM): LI
    fun postFilter(listItemModel: LI): Boolean {
        return true
    }
}

interface BrowseSortableEntityUseCase<DM : Any> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(entityId: String, query: String, sorted: Boolean): PagingSource<Int, DM>
}
