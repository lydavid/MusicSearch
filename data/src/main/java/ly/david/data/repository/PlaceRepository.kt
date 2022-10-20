package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.Place
import ly.david.data.domain.PlaceUiModel
import ly.david.data.domain.toPlaceUiModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.history.LookupHistory
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.place.toPlaceRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel

@Singleton
class PlaceRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val placeDao: PlaceDao,
    private val relationDao: RelationDao,
    private val lookupHistoryDao: LookupHistoryDao
) {
    private var place: PlaceUiModel? = null

    suspend fun lookupPlace(placeId: String): PlaceUiModel =
        place ?: run {

            // Use cached model.
            val placeRoomModel = placeDao.getPlace(placeId)
            if (placeRoomModel != null) {
                incrementOrInsertLookupHistory(placeRoomModel)
                return placeRoomModel.toPlaceUiModel()
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
            placeMusicBrainzModel.toPlaceUiModel()
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
