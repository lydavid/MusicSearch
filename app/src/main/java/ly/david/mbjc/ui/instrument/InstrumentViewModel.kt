package ly.david.mbjc.ui.instrument

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class InstrumentViewModel @Inject constructor(
    private val instrumentRepository: InstrumentRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupInstrument(instrumentId: String) = instrumentRepository.lookupInstrument(instrumentId).also {
        loadRelations(it.id)
    }
}
