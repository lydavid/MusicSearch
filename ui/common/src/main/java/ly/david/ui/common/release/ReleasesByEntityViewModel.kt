package ly.david.ui.common.release

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
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.ui.common.paging.IPagedList

abstract class ReleasesByEntityViewModel(
    private val entity: MusicBrainzEntity,
    private val getReleasesByEntity: GetReleasesByEntity,
) : ViewModel(), IPagedList<ReleaseListItemModel> {

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    override val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val paramState = combine(
        entityId,
        query,
        isRemote,
    ) { entityId, query, isRemote ->
        IPagedList.ViewModelState(
            entityId,
            query,
            isRemote,
        )
    }.distinctUntilChanged()

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    override val pagedEntities: Flow<PagingData<ReleaseListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query, isRemote) ->
                getReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                        isRemote = isRemote,
                    ),
                )
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)
    }
}
