package ly.david.mbjc.ui.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.history.LookupHistory
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.history.IncrementLookupHistoryUseCase
import ly.david.data.domain.work.WorkRepository
import ly.david.data.domain.work.WorkScaffoldModel
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class WorkScaffoldViewModel(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistoryUseCase: IncrementLookupHistoryUseCase,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.WORK
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val work: MutableStateFlow<WorkScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
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
                        incrementLookupHistoryUseCase(
                            LookupHistory(
                                mbid = workId,
                                title = title.value,
                                entity = entity,
                            )
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
