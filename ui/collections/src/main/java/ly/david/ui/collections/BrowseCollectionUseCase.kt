package ly.david.ui.collections

import androidx.paging.PagingSource
import ly.david.ui.common.paging.StoreEntityUseCase

interface BrowseCollectionUseCase<RM : Any> : StoreEntityUseCase {
    fun getLinkedEntitiesPagingSource(viewState: ICollectionPagedList.ViewModelState): PagingSource<Int, RM>
}
