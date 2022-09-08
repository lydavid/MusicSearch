package ly.david.mbjc.ui.relation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ly.david.mbjc.data.domain.Header
import ly.david.mbjc.data.domain.RelationUiModel
import ly.david.mbjc.data.domain.UiModel
import ly.david.mbjc.data.domain.toRelationUiModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig
import ly.david.mbjc.ui.common.paging.RelationsRemoteMediator

/**
 * Generic ViewModel that let us fetch [pagedRelations] given a [resourceId].
 */
internal abstract class RelationViewModel(relationDao: RelationDao) : ViewModel() {

    val resourceId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, ExperimentalPagingApi::class)
    val pagedRelations: Flow<PagingData<UiModel>> =
        resourceId.flatMapLatest { resourceId ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                remoteMediator = RelationsRemoteMediator(
                    hasRelationsBeenStored = { hasRelationsBeenStored() },
                    lookupRelations = { lookupRelationsAndStore(resourceId) }
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
     * This will determine whether we will call [lookupRelationsAndStore].
     *
     * So it makes the most sense for [lookupRelationsAndStore] to set this underlying query to true.
     */
    open suspend fun hasRelationsBeenStored(): Boolean = false

    /**
     * Query to delete resource relationships in Room.
     */
//    open suspend fun deleteLocalRelations() {}

    /**
     * This is responsible for making a lookup request for this resource's relationships,
     * then storing them into Room.
     *
     * Unlike browse requests, this is expected to only be called once.
     */
    open suspend fun lookupRelationsAndStore(resourceId: String) {}
}
