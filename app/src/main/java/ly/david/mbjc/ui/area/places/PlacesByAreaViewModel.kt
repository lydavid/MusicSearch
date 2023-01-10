package ly.david.mbjc.ui.area.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.toPlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.area.AreaPlace
import ly.david.data.persistence.area.AreaPlaceDao
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.persistence.place.toPlaceRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class PlacesByAreaViewModel @Inject constructor(
    private val pagedList: PagedList<PlaceRoomModel, PlaceListItemModel>,
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val placeDao: PlaceDao,
) : ViewModel(),
    IPagedList<PlaceListItemModel> by pagedList,
    BrowseResourceUseCase<PlaceRoomModel, PlaceListItemModel> {

    init {
        pagedList.scope = viewModelScope
        pagedList.useCase = this
    }

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = musicBrainzApiService.browsePlacesByArea(
            areaId = resourceId,
            offset = nextOffset
        )

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.PLACE,
                    localCount = response.places.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.PLACE, response.places.size)
        }

        val placeMusicBrainzModels = response.places
        placeDao.insertAll(placeMusicBrainzModels.map { it.toPlaceRoomModel() })
        areaPlaceDao.insertAll(
            placeMusicBrainzModels.map { place ->
                AreaPlace(
                    areaId = resourceId,
                    placeId = place.id
                )
            }
        )

        return placeMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.PLACE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.PLACE)?.localCount ?: 0

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
            areaPlaceDao.getPlacesByArea(
                areaId = resourceId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: PlaceRoomModel): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
