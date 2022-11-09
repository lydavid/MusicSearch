package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.LabelUiModel
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.release.IReleasesList
import ly.david.mbjc.ui.release.ReleasesList

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val releasesList: ReleasesList
) : ViewModel(), RecordLookupHistory, IReleasesList by releasesList {

    init {
        releasesList.scope = viewModelScope
        releasesList.repository = repository
    }

    suspend fun lookupLabel(labelId: String): LabelUiModel =
        repository.lookupLabel(labelId)
}
