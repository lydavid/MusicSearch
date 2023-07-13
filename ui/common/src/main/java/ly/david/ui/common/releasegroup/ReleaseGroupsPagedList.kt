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
import ly.david.data.domain.listitem.ListItemModel
import ly.david.data.domain.listitem.ListSeparator
import ly.david.data.domain.listitem.ReleaseGroupListItemModel
import ly.david.data.domain.listitem.toReleaseGroupListItemModel
import ly.david.data.domain.paging.BrowseEntityRemoteMediator
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.getDisplayTypes
import ly.david.data.room.releasegroup.ReleaseGroupForListItem
import ly.david.ui.common.paging.BrowseSortableEntityUseCase
import ly.david.ui.common.paging.SortablePagedList

/**
 * Generic implementation for handling paged release groups.
 *
 * Meant to be implemented by a ViewModel through delegation.
 *
 * The ViewModel should should assign [scope] and [useCase] in its init block.
 */
class ReleaseGroupsPagedList @Inject constructor() : SortablePagedList<ListItemModel> {

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val sorted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val paramState = combine(entityId, query, isRemote, sorted) { entityId, query, isRemote, sorted ->
        SortablePagedList.ViewModelState(entityId, query, isRemote, sorted)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseSortableEntityUseCase<ReleaseGroupForListItem>

    private fun getRemoteMediator(entityId: String) = BrowseEntityRemoteMediator<ReleaseGroupForListItem>(
        getRemoteEntityCount = { useCase.getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { useCase.getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = { useCase.deleteLinkedEntitiesByEntity(entityId) },
        browseEntity = { offset ->
            useCase.browseLinkedEntitiesAndStore(entityId, offset)
        }
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedEntities: Flow<PagingData<ListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query, isRemote, sorted) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(entityId).takeIf { isRemote },
                    pagingSourceFactory = { useCase.getLinkedEntitiesPagingSource(entityId, query, sorted) }
                ).flow.map { pagingData ->
                    pagingData
                        .map(ReleaseGroupForListItem::toReleaseGroupListItemModel)
                        .insertSeparators { rg1: ReleaseGroupListItemModel?, rg2: ReleaseGroupListItemModel? ->
                            if (sorted && rg2 != null &&
                                (rg1?.primaryType != rg2.primaryType || rg1?.secondaryTypes != rg2.secondaryTypes)
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
