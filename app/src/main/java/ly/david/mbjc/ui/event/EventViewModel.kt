package ly.david.mbjc.ui.event

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.repository.EventRepository
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupEvent(eventId: String) = eventRepository.lookupEvent(eventId).also {
        loadRelations(it.id)
    }
}
