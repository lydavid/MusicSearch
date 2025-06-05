package ly.david.musicsearch.shared.domain.event

import ly.david.musicsearch.shared.domain.details.EventDetailsModel

interface EventRepository {
    suspend fun lookupEvent(
        eventId: String,
        forceRefresh: Boolean,
    ): EventDetailsModel
}
