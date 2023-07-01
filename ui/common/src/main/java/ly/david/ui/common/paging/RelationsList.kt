package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
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
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.domain.paging.LookupResourceRemoteMediator
import ly.david.data.domain.paging.MusicBrainzPagingConfig
import ly.david.data.room.relation.HasRelations
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel

/**
 * A [ViewModel] implements this for [pagedRelations].
 */
interface IRelationsList {

    val resourceId: MutableStateFlow<String>
    val query: MutableStateFlow<String>

    val pagedRelations: Flow<PagingData<RelationListItemModel>>

    /**
     * Sets [resourceId] which will cause [pagedRelations] to get all relationships for this [resourceId].
     */
    fun loadRelations(resourceId: String) {
        this.resourceId.value = resourceId
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
class RelationsList @Inject constructor(
    private val relationDao: RelationDao
) : IRelationsList {

    data class State(
        val resourceId: String = "",
        val query: String = "",
    )

    override val resourceId: MutableStateFlow<String> = MutableStateFlow("")
    override val query: MutableStateFlow<String> = MutableStateFlow("")
    private val paramState = combine(resourceId, query) { resourceId, query ->
        State(resourceId, query)
    }.distinctUntilChanged()

    lateinit var scope: CoroutineScope
    lateinit var repository: RelationsListRepository

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    override val pagedRelations: Flow<PagingData<RelationListItemModel>> by lazy {
        paramState.filterNot { it.resourceId.isEmpty() }
            .flatMapLatest { (resourceId, query) ->
                Pager(
                    config = MusicBrainzPagingConfig.pagingConfig,
                    remoteMediator = LookupResourceRemoteMediator(
                        hasResourceBeenStored = { hasRelationsBeenStored() },
                        lookupResource = { forceRefresh ->
                            lookupRelationsAndStore(resourceId, forceRefresh = forceRefresh)
                        },
                        deleteLocalResource = {
                            deleteLocalRelations(resourceId)
                        }
                    ),
                    pagingSourceFactory = {
                        relationDao.getEntityRelationships(resourceId, "%$query%")
                    }
                ).flow.map { pagingData ->
                    pagingData.map { relation ->
                        relation.toRelationListItemModel()
                    }
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
        relationDao.hasRelations(resourceId.value)?.hasRelations == true

    /**
     * This is responsible for making a lookup request for this resource's relationships,
     * then storing them into Room.
     *
     * Unlike browse requests, this is expected to only be called once.
     */
    private suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {

        if (!forceRefresh) return

        val relations = mutableListOf<RelationRoomModel>()
        repository.lookupRelationsFromNetwork(resourceId)?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = resourceId,
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
                resourceId = resourceId.value,
                hasRelations = true
            )
        )
    }

    private suspend fun deleteLocalRelations(entityId: String) {
        relationDao.deleteRelationshipsByEntity(entityId)
    }
}
