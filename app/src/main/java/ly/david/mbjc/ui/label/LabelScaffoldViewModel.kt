package ly.david.mbjc.ui.label

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.LabelListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class LabelScaffoldViewModel @Inject constructor(
    private val repository: LabelRepository,
    override val lookupHistoryDao: LookupHistoryDao,
    private val relationsList: RelationsList,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.LABEL
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val label: MutableStateFlow<LabelListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        labelId: String,
        selectedTab: LabelTab
    ) {
        when (selectedTab) {
            LabelTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val labelListItemModel = repository.lookupLabel(labelId)
                        if (title.value.isEmpty()) {
                            title.value = labelListItemModel.getNameWithDisambiguation()
                        }
                        label.value = labelListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = labelId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            LabelTab.RELATIONSHIPS -> loadRelations(labelId)
            else -> {
                // Not handled here.
            }
        }
    }
}
