package ly.david.musicsearch.shared.domain.event

import ly.david.musicsearch.core.models.event.EventDetailsModel

interface EventRepository {
    suspend fun lookupEvent(eventId: String): EventDetailsModel
}
