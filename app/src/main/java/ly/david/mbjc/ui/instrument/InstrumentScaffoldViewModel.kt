package ly.david.mbjc.ui.instrument

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ly.david.data.domain.instrument.InstrumentRepository
import ly.david.data.domain.instrument.InstrumentScaffoldModel
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.common.network.RecoverableNetworkException
import ly.david.data.room.history.LookupHistoryDao
import ly.david.data.room.history.RecordLookupHistory
import ly.david.ui.common.MusicBrainzEntityViewModel
import ly.david.ui.common.paging.IRelationsList
import ly.david.ui.common.paging.RelationsList
import timber.log.Timber

@HiltViewModel
internal class InstrumentScaffoldViewModel @Inject constructor(
    private val repository: InstrumentRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(),
    MusicBrainzEntityViewModel,
    RecordLookupHistory,
    IRelationsList by relationsList {

    private var recordedLookup = false
    override val entity: MusicBrainzEntity = MusicBrainzEntity.INSTRUMENT
    override val title = MutableStateFlow("")
    override val isError = MutableStateFlow(false)

    val instrument: MutableStateFlow<InstrumentScaffoldModel?> = MutableStateFlow(null)

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    fun loadDataForTab(
        instrumentId: String,
        selectedTab: InstrumentTab,
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
                    } catch (ex: RecoverableNetworkException) {
                        Timber.e(ex)
                        isError.value = true
                    }

                    if (!recordedLookup) {
                        recordLookupHistory(
                            entityId = instrumentId,
                            entity = entity,
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
