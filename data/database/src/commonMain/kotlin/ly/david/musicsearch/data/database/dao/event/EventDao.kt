package ly.david.musicsearch.data.database.dao.event

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Event
import org.koin.core.annotation.Single

@Single
class EventDao(
    private val database: Database,
) {
    fun insert(event: Event) {
        database.eventQueries.insert(
            id = event.id,
            name = event.name,
            disambiguation = event.disambiguation,
            type = event.type,
            type_id = event.type_id,
            time = event.time,
            cancelled = event.cancelled,
        )
    }

    fun getEvent(eventId: String): Event {
        return database.eventQueries.getEvent(eventId).executeAsOne()
    }
}

//val eventDaoModule = module {
//    single {
//        EventDao(database = get())
//    }
//}
