package ly.david.mbjc.ui.event

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Event
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.event.EventDao
import ly.david.mbjc.data.persistence.toEventRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel

@Singleton
internal class EventRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventDao: EventDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var event: Event? = null

    suspend fun lookupEvent(eventId: String): Event =
        event ?: run {

            // Use cached model.
            val eventRoomModel = eventDao.getEvent(eventId)
            if (eventRoomModel != null) {
                incrementOrInsertLookupHistory(eventRoomModel)
                return eventRoomModel
            }

            val eventMusicBrainzModel = musicBrainzApiService.lookupEvent(eventId)
            eventDao.insert(eventMusicBrainzModel.toEventRoomModel())

            val eventRelations = mutableListOf<RelationRoomModel>()
            eventMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = eventId,
                    order = index
                )?.let { relationRoomModel ->
                    eventRelations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(eventRelations)

            incrementOrInsertLookupHistory(eventMusicBrainzModel)
            eventMusicBrainzModel
        }

    private suspend fun incrementOrInsertLookupHistory(event: Event) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = event.name,
                resource = MusicBrainzResource.EVENT,
                mbid = event.id
            )
        )
    }
}
