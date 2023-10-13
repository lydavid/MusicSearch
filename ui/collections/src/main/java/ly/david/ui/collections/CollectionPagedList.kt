package ly.david.ui.collections

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.musicsearch.domain.listitem.toCollectionListItemModel
import ly.david.musicsearch.domain.paging.BrowseEntityRemoteMediator
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import ly.david.ui.common.paging.IPagedList
import lydavidmusicsearchdatadatabase.Collection
import org.koin.core.annotation.Factory

interface ICollectionPagedList : IPagedList<CollectionListItemModel> {
    data class ViewModelState(
        val entityId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val showLocal: Boolean,
        val showRemote: Boolean,
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

@Factory
class CollectionPagedList : ICollectionPagedList {

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val showLocal: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val showRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(
        entityId,
        query,
        isRemote,
        showLocal,
        showRemote
    ) { entityId, query, isRemote, showLocal, showRemote ->
        ICollectionPagedList.ViewModelState(entityId, query, isRemote, showLocal, showRemote)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var useCase: BrowseCollectionUseCase<Collection>

    private fun getRemoteMediator(entityId: String) = BrowseEntityRemoteMediator<Collection>(
        getRemoteEntityCount = { useCase.getRemoteLinkedEntitiesCountByEntity(entityId) },
        getLocalEntityCount = { useCase.getLocalLinkedEntitiesCountByEntity(entityId) },
        deleteLocalEntity = { useCase.deleteLinkedEntitiesByEntity(entityId) },
        browseEntity = { offset ->
            useCase.browseLinkedEntitiesAndStore(entityId, offset)
        }
    )

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    override val pagedEntities: Flow<PagingData<CollectionListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { state ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = getRemoteMediator(state.entityId).takeIf { state.isRemote },
                    pagingSourceFactory = { useCase.getLinkedEntitiesPagingSource(state) }
                ).flow.map { pagingData ->
                    pagingData.map(Collection::toCollectionListItemModel)
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
