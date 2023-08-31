package ly.david.ui.collections.places

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.domain.listitem.toPlaceListItemModel
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.PlaceMusicBrainzModel
import ly.david.data.network.api.BrowsePlacesResponse
import ly.david.data.network.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.place.PlaceDao
import ly.david.data.room.place.PlaceRoomModel
import ly.david.data.room.place.toPlaceRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class PlacesByCollectionViewModel @Inject constructor(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    private val placeDao: PlaceDao,
    pagedList: PagedList<PlaceRoomModel, PlaceListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<PlaceRoomModel, PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
    byEntity = MusicBrainzEntity.PLACE,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApi.browsePlacesByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<PlaceMusicBrainzModel>) {
        placeDao.insertAll(musicBrainzModels.map { it.toPlaceRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { place ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = place.id
                )
            }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.PLACE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, PlaceRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getPlacesByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getPlacesByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: PlaceRoomModel): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
