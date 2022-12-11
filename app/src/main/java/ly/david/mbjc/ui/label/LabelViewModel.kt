package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.LabelListItemModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.relation.IRelationsList
import ly.david.mbjc.ui.relation.RelationsList

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupLabel(labelId: String): LabelListItemModel =
        repository.lookupLabel(labelId)
}
