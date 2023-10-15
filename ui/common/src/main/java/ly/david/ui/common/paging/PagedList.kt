package ly.david.ui.common.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.domain.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig

/**
 * Generic implementation for handling paged [ListItemModel]
 * whose source of truth is the local database.
 *
 * Meant to be implemented by a ViewModel through delegation.
 * The ViewModel should should assign [scope] and [useCase] in its init block.
 */
abstract class PagedList<LI : ListItemModel> : IPagedList<LI> {

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(entityId, query, isRemote) { entityId, query, isRemote ->
        IPagedList.ViewModelState(entityId, query, isRemote)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseEntityUseCase<LI>

    private fun getRemoteMediator(entityId: String) = BrowseEntityRemoteMediator<LI>(
        getRemoteEntityCount = { useCase.getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { useCase.getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = { useCase.deleteLinkedEntitiesByEntity(entityId) },
        browseEntity = { offset -> useCase.browseLinkedEntitiesAndStore(entityId, offset) },
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedEntities: Flow<PagingData<LI>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .distinctUntilChanged()
            .flatMapLatest { (entityId, query, isRemote) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(entityId).takeIf { isRemote },
                    pagingSourceFactory = { useCase.getLinkedEntitiesPagingSource(entityId, query) },
                )
                    .flow
                    .map { pagingData ->
                        pagingData.filter {
                            useCase.postFilter(it)
                        }
                    }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
