package ly.david.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.toPlaceListItemModel
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.AreaPlace
import ly.david.data.persistence.area.AreaPlaceDao
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.place.toPlaceRoomModel

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

        placeDao.insert(placeMusicBrainzModel.toPlaceRoomModel())

        // TODO: transaction
        placeMusicBrainzModel.area?.let { area ->
            areaDao.insert(area.toAreaRoomModel())
            areaPlaceDao.insert(
                AreaPlace(
                    areaId = area.id,
                    placeId = placeId
                )
            )
        }

        return placeMusicBrainzModel.toPlaceListItemModel()
    }

    override suspend fun lookupRelationsFromNetwork(resourceId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupPlace(
            placeId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS_EXCLUDE_EVENTS
        ).relations
    }
}
