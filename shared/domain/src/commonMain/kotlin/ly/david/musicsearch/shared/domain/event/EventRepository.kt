package ly.david.musicsearch.shared.domain.event

import ly.david.musicsearch.shared.domain.event.EventDetailsModel

interface EventRepository {
    suspend fun lookupEvent(eventId: String): EventDetailsModel
}
