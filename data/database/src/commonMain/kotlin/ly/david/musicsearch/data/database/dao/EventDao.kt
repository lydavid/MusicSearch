package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.event.EventScaffoldModel
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
                ),
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

    fun getEventForDetails(eventId: String): EventScaffoldModel? {
        return transacter.getEvent(
            eventId,
            mapper = ::toEventScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toEventScaffoldModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        time: String?,
        cancelled: Boolean?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = EventScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}
