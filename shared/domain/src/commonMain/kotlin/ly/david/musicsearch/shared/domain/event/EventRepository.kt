package ly.david.musicsearch.shared.domain.event

interface EventRepository {
    suspend fun lookupEvent(eventId: String): EventDetailsModel
}
