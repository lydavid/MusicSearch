package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Event
import ly.david.data.domain.EventUiModel
import ly.david.data.domain.toEventUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.toEventRoomModel
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel

@Singleton
class EventRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val eventDao: EventDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var event: EventUiModel? = null

    suspend fun lookupEvent(eventId: String): EventUiModel =
        event ?: run {

            // Use cached model.
            val eventRoomModel = eventDao.getEvent(eventId)
            if (eventRoomModel != null) {
                incrementOrInsertLookupHistory(eventRoomModel)
                return eventRoomModel.toEventUiModel()
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
            eventMusicBrainzModel.toEventUiModel()
        }

    private suspend fun incrementOrInsertLookupHistory(event: Event) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = event.name,
                resource = MusicBrainzResource.EVENT,
                mbid = event.id
            )
        )
    }
}
