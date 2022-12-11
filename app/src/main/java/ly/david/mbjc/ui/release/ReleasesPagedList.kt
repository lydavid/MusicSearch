package ly.david.mbjc.ui.release

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
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
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.domain.toReleaseListItemModel
import ly.david.data.paging.BrowseResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.repository.ReleasesListRepository
import ly.david.mbjc.ui.common.paging.PagedList

/**
 * Generic implementation for handling paged releases.
 *
 * Meant to be implemented by a ViewModel through delegation.
 *
 * The ViewModel should should assign [scope] and [repository] in its init block.
 */
internal class ReleasesPagedList @Inject constructor() : PagedList<ReleaseListItemModel> {

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val paramState = combine(resourceId, query) { resourceId, query ->
        PagedList.ViewModelState(resourceId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var repository: ReleasesListRepository

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedResources: Flow<PagingData<ReleaseListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = BrowseResourceRemoteMediator(
                        getRemoteResourceCount = { repository.getRemoteLinkedResourcesCountByResource(resourceId) },
                        getLocalResourceCount = { repository.getLocalLinkedResourcesCountByResource(resourceId) },
                        deleteLocalResource = { repository.deleteLinkedResourcesByResource(resourceId) },
                        browseResource = { offset ->
                            repository.browseLinkedResourcesAndStore(resourceId, offset)
                        }
                    ),
                    pagingSourceFactory = { repository.getLinkedResourcesPagingSource(resourceId, query) }
                ).flow.map { pagingData ->
                    pagingData.map {
                        it.toReleaseListItemModel()
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
