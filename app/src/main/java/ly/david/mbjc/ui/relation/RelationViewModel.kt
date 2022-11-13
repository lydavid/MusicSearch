package ly.david.mbjc.ui.relation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.data.domain.Header
import ly.david.data.domain.RelationUiModel
import ly.david.data.domain.UiModel
import ly.david.data.domain.toRelationUiModel
import ly.david.data.paging.LookupResourceRemoteMediator
import ly.david.data.paging.MusicBrainzPagingConfig
import ly.david.data.persistence.relation.HasRelationsRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.data.repository.RelationsListRepository

/**
 * A [ViewModel] implements this for [pagedRelations].
 */
internal interface IRelationsList {

    /**
     * Paginated [RelationUiModel] with [Header].
     */
    val pagedRelations: Flow<PagingData<UiModel>>

    /**
     * Sets [resourceId] which will cause [pagedRelations] to get all relationships for this [resourceId].
     */
    fun loadRelations(resourceId: String)

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
internal class RelationsList @Inject constructor(
    private val relationDao: RelationDao
) : IRelationsList {

    private val resourceId: MutableStateFlow<String> = MutableStateFlow("")

    lateinit var scope: CoroutineScope
    lateinit var repository: RelationsListRepository

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    override val pagedRelations: Flow<PagingData<UiModel>> by lazy {
        resourceId.filterNot { it.isEmpty() }
            .flatMapLatest { resourceId ->
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
                        relationDao.getRelationsForResource(resourceId)
                    }
                ).flow.map { pagingData ->
                    pagingData.map { relation ->
                        relation.toRelationUiModel()
                    }.insertSeparators { before: RelationUiModel?, _: RelationUiModel? ->
                        if (before == null) Header else null
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(scope)
    }

    override fun loadRelations(resourceId: String) {
        this.resourceId.value = resourceId
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

        // TODO: why was forceRefresh passed here? Currently only used in area

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

// TODO: convert rest
/**
 * Generic ViewModel that let us fetch [pagedRelations] given a [resourceId].
 */
internal abstract class RelationViewModel(private val relationDao: RelationDao) : ViewModel() {

    private val resourceId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedRelations: Flow<PagingData<UiModel>> =
        resourceId.filterNot { it.isEmpty() }
            .flatMapLatest { resourceId ->
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
                        relationDao.getRelationsForResource(resourceId)
                    }
                ).flow.map { pagingData ->
                    pagingData.map { relation ->
                        relation.toRelationUiModel()
                    }.insertSeparators { before: RelationUiModel?, _: RelationUiModel? ->
                        if (before == null) Header else null
                    }
                }
            }
            .distinctUntilChanged()
            .cachedIn(viewModelScope)

    /**
     * Sets [resourceId] which will cause [pagedRelations] to get all relationships for this [resourceId].
     */
    fun loadRelations(resourceId: String) {
        this.resourceId.value = resourceId
    }

    /**
     * This will determine whether we will call [lookupRelationsAndStore].
     *
     * So it makes the most sense for [lookupRelationsAndStore] to set this underlying query to true.
     */
    open suspend fun hasRelationsBeenStored(): Boolean =
        relationDao.getHasRelationsModel(resourceId.value)?.hasRelations == true

    /**
     * Query to delete resource relationships in Room.
     */
    private suspend fun deleteLocalRelations(resourceId: String) {
        relationDao.deleteRelationsByResource(resourceId)
    }

    /**
     * This is responsible for making a lookup request for this resource's relationships,
     * then storing them into Room.
     *
     * Unlike browse requests, this is expected to only be called once.
     */
    open suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean = false) {}

    /**
     * Indicate that we've stored a resource's relationships successfully.
     */
    suspend fun markResourceHasRelations() {
        relationDao.markResourceHasRelations(
            hasRelationsRoomModel = HasRelationsRoomModel(
                resourceId = resourceId.value,
                hasRelations = true
            )
        )
    }
}
