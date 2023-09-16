package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Mb_event

class EventDao(
    database: Database,
) {
    private val transacter = database.mb_eventQueries

    fun insert(event: EventMusicBrainzModel) {
        event.run {
            transacter.insert(
                Mb_event(
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

    fun getEvent(eventId: String): Mb_event? {
        return transacter.getEvent(eventId).executeAsOneOrNull()
    }
}
