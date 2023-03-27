package ly.david.mbjc.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.domain.toPlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.api.BrowsePlacesResponse
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.persistence.place.toPlaceRoomModel
import ly.david.data.persistence.relation.BrowseResourceCount
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseResourceUseCase
import ly.david.mbjc.ui.common.paging.IPagedList
import ly.david.mbjc.ui.common.paging.PagedList

internal abstract class PlacesByEntityViewModel(
    private val relationDao: RelationDao,
    private val placeDao: PlaceDao,
    private val pagedList: PagedList<PlaceRoomModel, PlaceListItemModel>,
) : ViewModel(),
    IPagedList<PlaceListItemModel> by pagedList,
    BrowseResourceUseCase<PlaceRoomModel, PlaceListItemModel> {

    init {
        pagedList.scope = viewModelScope
        this.also { pagedList.useCase = it }
    }

    abstract suspend fun browsePlacesByEntity(entityId: String, offset: Int): BrowsePlacesResponse

    abstract suspend fun insertAllLinkingModels(
        entityId: String,
        placeMusicBrainzModels: List<PlaceMusicBrainzModel>
    )

    override suspend fun browseLinkedResourcesAndStore(resourceId: String, nextOffset: Int): Int {
        val response = browsePlacesByEntity(resourceId, nextOffset)

        if (response.offset == 0) {
            relationDao.insertBrowseResourceCount(
                browseResourceCount = BrowseResourceCount(
                    resourceId = resourceId,
                    browseResource = MusicBrainzResource.PLACE,
                    localCount = response.musicBrainzModels.size,
                    remoteCount = response.count
                )
            )
        } else {
            relationDao.incrementLocalCountForResource(resourceId, MusicBrainzResource.PLACE, response.musicBrainzModels.size)
        }

        val placeMusicBrainzModels = response.musicBrainzModels
        placeDao.insertAll(placeMusicBrainzModels.map { it.toPlaceRoomModel() })
        insertAllLinkingModels(resourceId, placeMusicBrainzModels)

        return placeMusicBrainzModels.size
    }

    override suspend fun getRemoteLinkedResourcesCountByResource(resourceId: String): Int? =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.PLACE)?.remoteCount

    override suspend fun getLocalLinkedResourcesCountByResource(resourceId: String) =
        relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.PLACE)?.localCount ?: 0

    override fun transformRoomToListItemModel(roomModel: PlaceRoomModel): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
