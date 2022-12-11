package ly.david.mbjc.ui.place

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.PlaceRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class PlaceViewModel @Inject constructor(
    private val repository: PlaceRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupPlace(placeId: String): PlaceListItemModel =
        repository.lookupPlace(placeId)
}
