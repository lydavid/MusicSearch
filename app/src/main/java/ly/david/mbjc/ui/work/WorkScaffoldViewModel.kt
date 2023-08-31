package ly.david.mbjc.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.work.WorkRepository
import ly.david.data.domain.work.WorkScaffoldModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.RecoverableNetworkException
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import timber.log.Timber

@HiltViewModel
internal class WorkScaffoldViewModel @Inject constructor(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.WORK
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val work: MutableStateFlow<WorkScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
        workId: String,
        selectedTab: WorkTab,
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
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = workId,
                            entity = entity,
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
