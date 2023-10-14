package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.relation.usecase.GetEntityRelationships
import org.koin.core.annotation.Factory

/**
 * A [ViewModel] implements this for [pagedRelations].
 */
interface IRelationsList {

    val entityId: MutableStateFlow<String>
    val query: MutableStateFlow<String>

    val pagedRelations: Flow<PagingData<RelationListItemModel>>

    /**
     * Sets [entityId] which will cause [pagedRelations] to get all relationships for this [entityId].
     */
    fun loadRelations(entityId: String) {
        this.entityId.value = entityId
    }

    fun updateQuery(query: String) {
        this.query.value = query
    }
}

/**
 * Delegation that handles all the implementation of [IRelationsList].
 *
 * Meant to be injected into a [ViewModel]'s constructor.
 *
 * The ViewModel should should assign [scope] and [relationsListRepository] in its init block.
 */
@Factory
class RelationsList(
    private val getEntityRelationships: GetEntityRelationships,
) : IRelationsList {

    data class State(
        val entityId: String = "",
        val query: String = "",
    )

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(
        entityId,
        query,
    ) { entityId, query ->
        State(
            entityId,
            query,
        )
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var entity: MusicBrainzEntity

    @OptIn(
        ExperimentalCoroutinesApi::class,
    )
    override val pagedRelations: Flow<PagingData<RelationListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query) ->
                getEntityRelationships(
                    entity = entity,
                    entityId = entityId,
                    query = query,
                )
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }
}
