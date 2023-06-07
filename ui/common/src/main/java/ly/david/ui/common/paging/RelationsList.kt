package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
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
import ly.david.data.domain.Header
import ly.david.data.domain.ListItemModel
import ly.david.data.domain.RelationListItemModel
import ly.david.data.domain.toRelationListItemModel
import ly.david.data.paging.LookupResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.room.relation.HasRelationsRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel
import ly.david.data.repository.RelationsListRepository

/**
 * A [ViewModel] implements this for [pagedRelations].
 */
interface IRelationsList {

    val resourceId: MutableStateFlow<String>
    val query: MutableStateFlow<String>

    /**
     * Paginated [RelationListItemModel] with [Header].
     */
    val pagedRelations: Flow<PagingData<ListItemModel>>

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

    suspend fun markResourceHasRelations()
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
    override val pagedRelations: Flow<PagingData<ListItemModel>> by lazy {
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
                        if (query.isEmpty()) {
                            relationDao.getRelationsForResource(resourceId)
                        } else {
                            relationDao.getRelationsForResourceFiltered(resourceId, "%$query%")
                        }
                    }
                ).flow.map { pagingData ->
                    pagingData.map { relation ->
                        relation.toRelationListItemModel()
                    }.insertSeparators { before: RelationListItemModel?, after: RelationListItemModel? ->
                        // We used to put details as a Header in a paged list
                        // but it makes more sense to put it in its own tab where it won't interfere with things
                        // like refreshing and counting lazyPagingItems for an empty state.
                        // TODO: consider removing Header
                        if (before == null && after != null) Header else null
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
        relationDao.getHasRelationsModel(resourceId.value)?.hasRelations == true

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

        markResourceHasRelations()
    }

    /**
     * Indicate that we've stored a resource's relationships successfully.
     */
    override suspend fun markResourceHasRelations() {
        relationDao.markResourceHasRelations(
            hasRelationsRoomModel = HasRelationsRoomModel(
                resourceId = resourceId.value,
                hasRelations = true
            )
        )
    }

    /**
     * Query to delete resource relationships in Room.
     */
    private suspend fun deleteLocalRelations(resourceId: String) {
        relationDao.deleteRelationsByResource(resourceId)
    }
}
