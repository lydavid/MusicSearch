package ly.david.mbjc.ui.instrument

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.repository.InstrumentRepository
import ly.david.mbjc.ui.common.history.RecordLookupHistory
import ly.david.mbjc.ui.common.paging.IRelationsList
import ly.david.mbjc.ui.common.paging.RelationsList

@HiltViewModel
internal class InstrumentViewModel @Inject constructor(
    private val repository: InstrumentRepository,
    private val relationsList: RelationsList,
    override val lookupHistoryDao: LookupHistoryDao,
) : ViewModel(), RecordLookupHistory,
    IRelationsList by relationsList {

    override val resource: MusicBrainzResource = MusicBrainzResource.INSTRUMENT

    init {
        relationsList.scope = viewModelScope
        relationsList.repository = repository
    }

    suspend fun lookupInstrument(instrumentId: String) =
        repository.lookupInstrument(instrumentId)
}
