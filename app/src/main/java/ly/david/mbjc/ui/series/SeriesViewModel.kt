package ly.david.mbjc.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.SeriesRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class SeriesViewModel @Inject constructor(
    private val repository: SeriesRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupSeries(seriesId: String) =
        repository.lookupSeries(seriesId)
}
