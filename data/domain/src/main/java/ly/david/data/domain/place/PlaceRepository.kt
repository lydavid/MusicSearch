package ly.david.data.domain.place

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.domain.listitem.toPlaceListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.area.AreaDao
import ly.david.data.room.area.places.AreaPlace
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.area.toAreaRoomModel
import ly.david.data.room.place.PlaceDao
import ly.david.data.room.place.toPlaceRoomModel

@Singleton
class PlaceRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val areaDao: AreaDao,
) : RelationsListRepository {

    suspend fun lookupPlace(placeId: String): PlaceListItemModel {
        val placeRoomModel = placeDao.getPlace(placeId)
        if (placeRoomModel != null) {
            return placeRoomModel.toPlaceListItemModel()
        }

        val placeMusicBrainzModel = musicBrainzApiService.lookupPlace(placeId)

        areaDao.withTransaction {
            placeDao.insert(placeMusicBrainzModel.toPlaceRoomModel())
            placeMusicBrainzModel.area?.let { area ->
                areaDao.insert(area.toAreaRoomModel())
                areaPlaceDao.insert(
                    AreaPlace(
                        areaId = area.id,
                        placeId = placeId
                    )
                )
            }
        }

        return placeMusicBrainzModel.toPlaceListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupPlace(
            placeId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_EVENTS
        ).relations
    }
}
