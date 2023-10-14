package ly.david.musicsearch.domain.event

import ly.david.musicsearch.data.core.event.EventScaffoldModel

interface EventRepository {
    suspend fun lookupEvent(eventId: String): EventScaffoldModel
}
