package ly.david.mbjc.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Place
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.network.api.MusicBrainzApiService
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.data.persistence.place.PlaceDao
import ly.david.mbjc.data.persistence.place.toPlaceRoomModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel

@Singleton
internal class PlaceRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val placeDao: PlaceDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var place: Place? = null

    suspend fun lookupPlace(placeId: String): Place =
        place ?: run {

            // Use cached model.
            val placeRoomModel = placeDao.getPlace(placeId)
            if (placeRoomModel != null) {
                incrementOrInsertLookupHistory(placeRoomModel)
                return placeRoomModel
            }

            val placeMusicBrainzModel = musicBrainzApiService.lookupPlace(placeId)
            placeDao.insert(placeMusicBrainzModel.toPlaceRoomModel())

            // TODO: insert its area

            val relations = mutableListOf<RelationRoomModel>()
            placeMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = placeId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)

            incrementOrInsertLookupHistory(placeMusicBrainzModel)
            placeMusicBrainzModel
        }

    private suspend fun incrementOrInsertLookupHistory(place: Place) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                title = place.name,
                resource = MusicBrainzResource.PLACE,
                mbid = place.id
            )
        )
    }
}
