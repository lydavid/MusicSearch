package ly.david.mbjc.ui.instrument

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.InstrumentListItemModel
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.InstrumentRepository
import ly.david.mbjc.ui.common.MusicBrainzResourceViewModel
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class InstrumentScaffoldViewModel @Inject constructor(
    private val repository: InstrumentRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), MusicBrainzResourceViewModel, RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val resource: MusicBrainzResource = MusicBrainzResource.INSTRUMENT
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val instrument: MutableStateFlow<InstrumentListItemModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun onSelectedTabChange(
        instrumentId: String,
        selectedTab: InstrumentTab
    ) {
        when (selectedTab) {
            InstrumentTab.DETAILS -> {
                viewModelScope.launch {
                    try {
                        val eventListItemModel = repository.lookupInstrument(instrumentId)
                        if (title.value.isEmpty()) {
                            title.value = eventListItemModel.getNameWithDisambiguation()
                        }
                        instrument.value = eventListItemModel
                        isError.value = false
                    } catch (ex: Exception) {
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            resourceId = instrumentId,
                            resource = resource,
                            summary = title.value
                        )
                        recordedLookup = true
                    }
                }
            }
            InstrumentTab.RELATIONSHIPS -> loadRelations(instrumentId)
            else -> {
                // Not handled here.
            }
        }
    }
}
