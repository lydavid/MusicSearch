package ly.david.mbjc.ui.collections

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import androidx.paging.map
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.domain.CollectionListItemModel
import ly.david.data.domain.toCollectionListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.room.RoomModel
import ly.david.data.room.collection.CollectionWithEntities
import ly.david.ui.common.paging.IPagedList
import ly.david.ui.common.paging.StoreResourceUseCase

interface ICollectionPagedList : IPagedList<CollectionListItemModel> {
    data class ViewModelState(
        val resourceId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val showLocal: Boolean,
        val showRemote: Boolean
    )

    val showLocal: MutableStateFlow<Boolean>
    val showRemote: MutableStateFlow<Boolean>

    fun updateShowLocal(show: Boolean) {
        this.showLocal.value = show
    }

    fun updateShowRemote(show: Boolean) {
        this.showRemote.value = show
    }
}

interface BrowseCollectionUseCase<RM : RoomModel> : StoreResourceUseCase {
    fun getLinkedResourcesPagingSource(viewState: ICollectionPagedList.ViewModelState): PagingSource<Int, RM>
}

class CollectionPagedList @Inject constructor() : ICollectionPagedList {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val showLocal: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val showRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(
        resourceId,
        query,
        isRemote,
        showLocal,
        showRemote
    ) { resourceId, query, isRemote, showLocal, showRemote ->
        ICollectionPagedList.ViewModelState(resourceId, query, isRemote, showLocal, showRemote)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseCollectionUseCase<CollectionWithEntities>

    private fun getRemoteMediator(resourceId: String) = BrowseResourceRemoteMediator<CollectionWithEntities>(
        getRemoteResourceCount = { useCase.getRemoteLinkedResourcesCountByResource(resourceId) },
        getLocalResourceCount = { useCase.getLocalLinkedResourcesCountByResource(resourceId) },
        deleteLocalResource = { useCase.deleteLinkedResourcesByResource(resourceId) },
        browseResource = { offset ->
            useCase.browseLinkedResourcesAndStore(resourceId, offset)
        }
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedResources: Flow<PagingData<CollectionListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { state ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(state.resourceId).takeIf { state.isRemote },
                    pagingSourceFactory = { useCase.getLinkedResourcesPagingSource(state) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toCollectionListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
