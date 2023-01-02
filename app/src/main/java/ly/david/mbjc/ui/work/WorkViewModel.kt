package ly.david.mbjc.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.WorkListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.WorkRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class WorkViewModel @Inject constructor(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.WORK
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val work: MutableStateFlow<WorkListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        workId: String,
        selectedTab: WorkTab
    ) {
        when (selectedTab) {
            WorkTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val workListItemModel = repository.lookupWork(workId)
                        if (title.value.isEmpty()) {
                            title.value = workListItemModel.getNameWithDisambiguation()
                        }
                        work.value = workListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = workId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            WorkTab.RELATIONSHIPS -> loadRelations(workId)
            else -> {
                // Not handled here.
            }
        }
    }
}
