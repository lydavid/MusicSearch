package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Event

class EventDao(
    database: Database,
) : EntityDao {
    override val transacter = database.eventQueries

    fun insert(event: EventMusicBrainzModel) {
        event.run {
            transacter.insert(
                Event(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    time = time,
                    cancelled = cancelled,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                )
            )
        }
    }

    fun insertAll(events: List<EventMusicBrainzModel>) {
        transacter.transaction {
            events.forEach { event ->
                insert(event)
            }
        }
    }

    fun getEvent(eventId: String): Event? {
        return transacter.getEvent(eventId).executeAsOneOrNull()
    }
}
