package ly.david.ui.common.paging

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
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
import ly.david.musicsearch.domain.paging.LookupEntityRemoteMediator
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.relation.usecase.LookupRelationsAndStore
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
    private val relationRepository: RelationRepository,
    private val lookupRelationsAndStore: LookupRelationsAndStore,
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
    lateinit var entity: MusicBrainzEntity

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
                        relationRepository.getEntityRelationshipsExcludingUrls(
                            entityId = entityId,
                            query = query,
                        )
                    }
                ).flow
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
        relationRepository.hasRelationsBeenSavedFor(entityId.value)

    /**
     * This is responsible for making a lookup request for this resource's relationships,
     * then storing them into our database.
     *
     * Unlike browse requests, this is expected to only be called once.
     */
    private suspend fun lookupRelationsAndStore(entityId: String, forceRefresh: Boolean) {
        if (!forceRefresh) return

        lookupRelationsAndStore(entity, entityId)
//        val relations = relationsListRepository.lookupRelationsFromNetwork(entityId)
//        val relationWithOrderList = relations?.mapIndexedNotNull { index, relationMusicBrainzModel ->
//            relationMusicBrainzModel.toRelationDatabaseModel(
//                entityId = entityId,
//                order = index,
//            )
//        }
//        relationRepository.insertAllRelationsExcludingUrls(
//            entityId = entityId,
//            relationMusicBrainzModels = relationWithOrderList,
//        )
    }

    private fun deleteLocalRelations(entityId: String) {
        relationRepository.deleteEntityRelationships(entityId)
    }
}
