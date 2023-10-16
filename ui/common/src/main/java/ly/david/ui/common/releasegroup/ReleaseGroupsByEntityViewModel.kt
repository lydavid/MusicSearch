package ly.david.ui.common.releasegroup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.releasegroup.usecase.GetReleaseGroupsByEntity
import ly.david.ui.common.paging.SortablePagedList

abstract class ReleaseGroupsByEntityViewModel(
    private val entity: MusicBrainzEntity,
    private val getReleaseGroupsByEntity: GetReleaseGroupsByEntity,
) : ViewModel(),
    SortablePagedList<ListItemModel> {

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val sorted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val paramState = combine(
        entityId,
        query,
        isRemote,
        sorted,
    ) { entityId, query, isRemote, sorted ->
        SortablePagedList.ViewModelState(
            entityId,
            query,
            isRemote,
            sorted,
        )
    }.distinctUntilChanged()

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    override val pagedEntities: Flow<PagingData<ListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query, isRemote, sorted) ->
                getReleaseGroupsByEntity(
                    entityId = entityId,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                        sorted = sorted,
                    ),
                )
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }
}
