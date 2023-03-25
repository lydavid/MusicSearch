package ly.david.mbjc.ui.collections.places

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.PlaceListItemModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.api.BrowsePlacesResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.place.PlaceDao
import ly.david.data.persistence.place.PlaceRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.PlacesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class PlacesByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    placeDao: PlaceDao,
    pagedList: PagedList<PlaceRoomModel, PlaceListItemModel>,
) : PlacesByEntityViewModel(
    relationDao = relationDao,
    placeDao = placeDao,
    pagedList = pagedList
) {

    override suspend fun browsePlacesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApiService.browsePlacesByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, placeMusicBrainzModels: List<PlaceMusicBrainzModel>) {
        collectionEntityDao.insertAll(
            placeMusicBrainzModels.map { place ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = place.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteCollectionEntityLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.PLACE)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, PlaceRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getPlacesByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getPlacesByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }
}
