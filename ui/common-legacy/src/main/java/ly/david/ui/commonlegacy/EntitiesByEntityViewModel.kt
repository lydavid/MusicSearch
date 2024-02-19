package ly.david.ui.commonlegacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
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
import ly.david.musicsearch.domain.base.usecase.GetEntitiesByEntity

abstract class EntitiesByEntityViewModel<LI : ListItemModel>(
    private val entity: MusicBrainzEntity,
    private val getEntitiesByEntity: GetEntitiesByEntity<LI>,
) : ViewModel() {

    data class ViewModelState(
        val entityId: String = "",
        val query: String = "",
        val isRemote: Boolean = true,
        val sorted: Boolean = false,
    )

    private val entityId: MutableStateFlow<String> = MutableStateFlow("")
    private val query: MutableStateFlow<String> = MutableStateFlow("")
    private val isRemote: MutableStateFlow<Boolean> = MutableStateFlow(true)
    private val sorted: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val paramState = combine(
        entityId,
        query,
        isRemote,
        sorted,
    ) { entityId, query, isRemote, sorted ->
        ViewModelState(
            entityId = entityId,
            query = query,
            isRemote = isRemote,
            sorted = sorted,
        )
    }.distinctUntilChanged()

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    val pagedEntities: Flow<PagingData<LI>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query, isRemote, sorted) ->
                getEntitiesByEntity(
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

    fun updateSorted(sorted: Boolean) {
        this.sorted.value = sorted
    }

    fun loadPagedEntities(entityId: String) {
        this.entityId.value = entityId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }

    fun setRemote(isRemote: Boolean) {
        this.isRemote.value = isRemote
    }
}
