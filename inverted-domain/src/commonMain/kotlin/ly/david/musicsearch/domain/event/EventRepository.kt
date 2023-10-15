package ly.david.musicsearch.domain.event

import ly.david.musicsearch.core.models.event.EventScaffoldModel

interface EventRepository {
    suspend fun lookupEvent(eventId: String): EventScaffoldModel
}
