package ly.david.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
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
import ly.david.data.domain.ListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.room.RoomModel

/**
 * Generic implementation for handling paged [RoomModel]/[ListItemModel].
 *
 * Meant to be implemented by a ViewModel through delegation.
 * The ViewModel should should assign [scope] and [useCase] in its init block.
 */
class PagedList<RM : RoomModel, LI : ListItemModel> @Inject constructor() : IPagedList<LI> {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(resourceId, query, isRemote) { resourceId, query, isRemote ->
        IPagedList.ViewModelState(resourceId, query, isRemote)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseResourceUseCase<RM, LI>

    private fun getRemoteMediator(resourceId: String) = BrowseResourceRemoteMediator<RM>(
        getRemoteResourceCount = { useCase.getRemoteLinkedResourcesCountByResource(resourceId) },
        getLocalResourceCount = { useCase.getLocalLinkedResourcesCountByResource(resourceId) },
        deleteLocalResource = { useCase.deleteLinkedResourcesByResource(resourceId) },
        browseResource = { offset -> useCase.browseLinkedResourcesAndStore(resourceId, offset) }
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedResources: Flow<PagingData<LI>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .distinctUntilChanged()
            .flatMapLatest { (resourceId, query, isRemote) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(resourceId).takeIf { isRemote },
                    pagingSourceFactory = { useCase.getLinkedResourcesPagingSource(resourceId, query) }
                )
                    .flow
                    .map { pagingData ->
                        pagingData.map {
                            useCase.transformRoomToListItemModel(it)
                        }.filter {
                            useCase.postFilter(it)
                        }
                    }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
