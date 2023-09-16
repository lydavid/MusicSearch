package ly.david.musicsearch.data.database.dao.event

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Event

class EventDao(
    private val database: Database,
) {
    fun insert(event: EventMusicBrainzModel) {
        event.run {
            database.eventQueries.insert(
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
        }
    }

    fun getEvent(eventId: String): Event {
        return database.eventQueries.getEvent(eventId).executeAsOne()
    }
}
