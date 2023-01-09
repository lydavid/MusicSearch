package ly.david.mbjc.ui.area

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.AreaScaffoldModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.AreaRepository
import ly.david.data.showReleases
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class AreaScaffoldViewModel @Inject constructor(
    private val repository: AreaRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.AREA
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    var tabs: MutableStateFlow<List<AreaTab>> = MutableStateFlow(AreaTab.values().filter { it != AreaTab.RELEASES })
    val area: MutableStateFlow<AreaScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        areaId: String,
        selectedTab: AreaTab
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
                            tabs.value = AreaTab.values().toList()
                        }
                        area.value = areaScaffoldModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = areaId,
                            resource = resource,
                            summary = title.value
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
