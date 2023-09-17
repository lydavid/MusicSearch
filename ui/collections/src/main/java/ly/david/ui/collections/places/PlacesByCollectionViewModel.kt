package ly.david.ui.collections.places

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.data.domain.listitem.toPlaceListItemModel
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.relation.RoomRelationDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.place.PlacesPagedList
import lydavidmusicsearchdatadatabase.Place
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class PlacesByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RoomRelationDao,
    private val placeDao: PlaceDao,
    pagedList: PlacesPagedList,
) : BrowseEntitiesByEntityViewModel<Place, PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
    byEntity = MusicBrainzEntity.PLACE,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApi.browsePlacesByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<PlaceMusicBrainzModel>) {
        placeDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            collectionId = entityId, // TODO: confusingly named, but this is correct
            entityIds = musicBrainzModels.map { place -> place.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.deleteAllFromCollection(entityId)
        relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.PLACE)
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, Place> =
        collectionEntityDao.getPlacesByCollection(
            collectionId = entityId,
            query = "%$query%",
        )

    override fun transformRoomToListItemModel(roomModel: Place): PlaceListItemModel {
        return roomModel.toPlaceListItemModel()
    }
}
