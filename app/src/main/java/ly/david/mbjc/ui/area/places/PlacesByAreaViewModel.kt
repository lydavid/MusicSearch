package ly.david.mbjc.ui.area.places

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.domain.listitem.toPlaceListItemModel
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.area.places.AreaPlace
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.place.PlaceDao
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.toPlaceRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.place.PlacesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class PlacesByAreaViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val areaPlaceDao: AreaPlaceDao,
    private val relationDao: RoomRelationDao,
    private val placeDao: PlaceDao,
    pagedList: PlacesPagedList,
) : BrowseEntitiesByEntityViewModel<PlaceRoomModel, PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
    byEntity = MusicBrainzEntity.PLACE,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApi.browsePlacesByArea(
            areaId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<PlaceMusicBrainzModel>) {
        placeDao.insertAll(musicBrainzModels.map { it.toPlaceRoomModel() })
        areaPlaceDao.insertAll(
            musicBrainzModels.map { place ->
                AreaPlace(
                    areaId = entityId,
                    placeId = place.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        areaPlaceDao.deletePlacesByArea(entityId)
        relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.PLACE)
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, PlaceRoomModel> = when {
        query.isEmpty() -> {
            areaPlaceDao.getPlacesByArea(entityId)
        }
        else -> {
            areaPlaceDao.getPlacesByAreaFiltered(
                areaId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: PlaceRoomModel): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
