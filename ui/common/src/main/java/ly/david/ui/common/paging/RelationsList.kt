package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
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
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.domain.paging.LookupEntityRemoteMediator
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.relation.HasRelations
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel
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

    suspend fun hasRelationsBeenStored(): Boolean

    suspend fun markEntityHasRelations()
}

/**
 * Delegation that handles all the implementation of [IRelationsList].
 *
 * Meant to be injected into a [ViewModel]'s constructor.
 *
 * The ViewModel should should assign [scope] and [repository] in its init block.
 */
@Factory
class RelationsList(
    private val relationDao: RelationDao,
) : IRelationsList {

    data class State(
        val entityId: String = "",
        val query: String = "",
    )

    override val entityId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(entityId, query) { entityId, query ->
        State(entityId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var repository: RelationsListRepository

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    override val pagedRelations: Flow<PagingData<RelationListItemModel>> by lazy {
        paramState.filterNot { it.entityId.isEmpty() }
            .flatMapLatest { (entityId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupEntityRemoteMediator(
                        hasEntityBeenStored = { hasRelationsBeenStored() },
                        lookupEntity = { forceRefresh ->
                            lookupRelationsAndStore(entityId, forceRefresh = forceRefresh)
                        },
                        deleteLocalEntity = {
                            deleteLocalRelations(entityId)
                        }
                    ),
                    pagingSourceFactory = {
                        relationDao.getEntityRelationships(
                            entityId = entityId,
                            query = "%$query%",
                        )
                    }
                ).flow.map { pagingData ->
                    pagingData.map(RelationRoomModel::toRelationListItemModel)
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }

    /**
     * This will determine whether we will call [lookupRelationsAndStore].
     *
     * So it makes the most sense for [lookupRelationsAndStore] to set this underlying query to true.
     */
    override suspend fun hasRelationsBeenStored(): Boolean =
        relationDao.hasRelations(entityId.value)?.hasRelations == true

    /**
     * This is responsible for making a lookup request for this resource's relationships,
     * then storing them into Room.
     *
     * Unlike browse requests, this is expected to only be called once.
     */
    private suspend fun lookupRelationsAndStore(entityId: String, forceRefresh: Boolean) {
        if (!forceRefresh) return

        val relations = mutableListOf<RelationRoomModel>()
        repository.lookupRelationsFromNetwork(entityId)?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                entityId = entityId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)

        markEntityHasRelations()
    }

    override suspend fun markEntityHasRelations() {
        relationDao.markEntityHasRelations(
            hasRelations = HasRelations(
                entityId = entityId.value,
                hasRelations = true
            )
        )
    }

    private suspend fun deleteLocalRelations(entityId: String) {
        relationDao.deleteRelationshipsByEntity(entityId)
    }
}
