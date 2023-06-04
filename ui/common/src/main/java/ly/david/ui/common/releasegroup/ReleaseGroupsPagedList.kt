package ly.david.ui.common.releasegroup

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
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
import ly.david.data.domain.ListSeparator
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.domain.toReleaseGroupListItemModel
import ly.david.data.getDisplayTypes
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.releasegroup.ReleaseGroupForListItem
import ly.david.ui.common.paging.BrowseSortableResourceUseCase
import ly.david.ui.common.paging.SortablePagedList

/**
 * Generic implementation for handling paged release groups.
 *
 * Meant to be implemented by a ViewModel through delegation.
 *
 * The ViewModel should should assign [scope] and [useCase] in its init block.
 */
class ReleaseGroupsPagedList @Inject constructor() : SortablePagedList<ListItemModel> {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val sorted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val paramState = combine(resourceId, query, isRemote, sorted) { resourceId, query, isRemote, sorted ->
        SortablePagedList.ViewModelState(resourceId, query, isRemote, sorted)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseSortableResourceUseCase<ReleaseGroupForListItem>

    private fun getRemoteMediator(resourceId: String) = BrowseResourceRemoteMediator<ReleaseGroupForListItem>(
        getRemoteResourceCount = { useCase.getRemoteLinkedResourcesCountByResource(resourceId) },
        getLocalResourceCount = { useCase.getLocalLinkedResourcesCountByResource(resourceId) },
        deleteLocalResource = { useCase.deleteLinkedResourcesByResource(resourceId) },
        browseResource = { offset ->
            useCase.browseLinkedResourcesAndStore(resourceId, offset)
        }
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedResources: Flow<PagingData<ListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query, isRemote, sorted) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(resourceId).takeIf { isRemote },
                    pagingSourceFactory = { useCase.getLinkedResourcesPagingSource(resourceId, query, sorted) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseGroupListItemModel()
                    }
                        .insertSeparators { rg1: ReleaseGroupListItemModel?, rg2: ReleaseGroupListItemModel? ->
                            if (sorted && rg2 != null &&
                                (rg1?.primaryType != rg2.primaryType ||
                                    rg1?.secondaryTypes != rg2.secondaryTypes)
                            ) {
                                ListSeparator(
                                    id = "${rg1?.id}_${rg2.id}",
                                    text = rg2.getDisplayTypes()
                                )
                            } else {
                                null
                            }
                        }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
