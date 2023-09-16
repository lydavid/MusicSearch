package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.showReleases
import ly.david.data.domain.area.AreaRepository
import ly.david.data.domain.area.AreaScaffoldModel
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
internal class AreaScaffoldViewModel(
    private val repository: AreaRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.AREA
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    var areaTabs: MutableStateFlow<List<AreaTab>> = MutableStateFlow(AreaTab.values().filter { it != AreaTab.RELEASES })
    val area: MutableStateFlow<AreaScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.relationsListRepository = repository
    }

    fun loadDataForTab(
        areaId: String,
        selectedTab: AreaTab,
    ) {
        when (selectedTab) {
            AreaTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val areaScaffoldModel = repository.lookupArea(areaId)
                        if (title.value.isEmpty()) {
                            title.value = areaScaffoldModel.getNameWithDisambiguation()
                        }
                        if (areaScaffoldModel.showReleases()) {
                            areaTabs.value = AreaTab.values().toList()
                        }
                        area.value = areaScaffoldModel
                        isError.value = false
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = areaId,
                            entity = entity,
                            summary = title.value,
                            searchHint = area.value?.sortName ?: ""
                        )
                        recordedLookup = true
                    }
                }
            }

            AreaTab.RELATIONSHIPS -> loadRelations(areaId)
            else -> {
                // Not handled here.
            }
        }
    }
}
