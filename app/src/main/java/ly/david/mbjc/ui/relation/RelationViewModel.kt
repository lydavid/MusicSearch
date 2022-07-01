package ly.david.mbjc.ui.relation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.ui.common.paging.MusicBrainzPagingConfig

/**
 * Generic ViewModel that let us fetch [pagedRelations] given a [resourceId].
 */
internal abstract class RelationViewModel(relationDao: RelationDao) : ViewModel() {

    val resourceId: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagedRelations: Flow<PagingData<UiModel>> =
        resourceId.flatMapLatest { recordingId ->
            Pager(
                config = MusicBrainzPagingConfig.pagingConfig,
                pagingSourceFactory = {
                    relationDao.getRelationsForResource(recordingId)
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
}
