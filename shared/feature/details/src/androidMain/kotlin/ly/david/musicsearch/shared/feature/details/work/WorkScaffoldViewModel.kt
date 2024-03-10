package ly.david.musicsearch.shared.feature.details.work

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.work.WorkScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.work.WorkRepository
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WorkScaffoldViewModel(
    private val repository: WorkRepository,
    private val relationsList: RelationsList,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val logger: Logger,
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
        relationsList.entity = entity
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
                        logger.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        incrementLookupHistory(
                            LookupHistory(
                                mbid = workId,
                                title = title.value,
                                entity = entity,
                            ),
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
