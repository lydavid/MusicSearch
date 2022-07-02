package ly.david.mbjc.ui.place

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.Place
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.LookupHistory
import ly.david.mbjc.data.persistence.LookupHistoryDao
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.data.persistence.RelationRoomModel
import ly.david.mbjc.data.persistence.place.PlaceDao
import ly.david.mbjc.data.persistence.toPlaceRoomModel
import ly.david.mbjc.data.persistence.toRelationRoomModel

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
                summary = place.name,
                resource = MusicBrainzResource.PLACE,
                mbid = place.id
            )
        )
    }
}
