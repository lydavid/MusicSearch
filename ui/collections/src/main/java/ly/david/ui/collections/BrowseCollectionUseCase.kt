package ly.david.ui.collections

import androidx.paging.PagingSource
import ly.david.data.room.RoomModel
import ly.david.ui.common.paging.StoreEntityUseCase

interface BrowseCollectionUseCase<RM : RoomModel> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(viewState: ICollectionPagedList.ViewModelState): PagingSource<Int, RM>
}
