package ly.david.mbjc.ui.area.places

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.toPlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.api.BrowsePlacesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.area.places.AreaPlace
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.place.PlaceDao
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.toPlaceRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class PlacesByAreaViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaPlaceDao: AreaPlaceDao,
    private val relationDao: RelationDao,
    private val placeDao: PlaceDao,
    pagedList: PagedList<PlaceRoomModel, PlaceListItemModel>,
) : BrowseEntitiesByEntityViewModel<PlaceRoomModel, PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
    byEntity = MusicBrainzResource.PLACE,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApiService.browsePlacesByArea(
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

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        areaPlaceDao.deletePlacesByArea(resourceId)
        relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.PLACE)
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, PlaceRoomModel> = when {
        query.isEmpty() -> {
            areaPlaceDao.getPlacesByArea(resourceId)
        }
        else -> {
            areaPlaceDao.getPlacesByAreaFiltered(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: PlaceRoomModel): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
