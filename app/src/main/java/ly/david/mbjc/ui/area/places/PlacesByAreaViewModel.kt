package ly.david.mbjc.ui.area.places

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.place.PlacesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class PlacesByAreaViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val areaPlaceDao: AreaPlaceDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val placeDao: PlaceDao,
    pagedList: PlacesPagedList,
) : BrowseEntitiesByEntityViewModel<PlaceListItemModel, PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
    byEntity = MusicBrainzEntity.PLACE,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowsePlacesResponse {
        return musicBrainzApi.browsePlacesByArea(
            areaId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<PlaceMusicBrainzModel>) {
        placeDao.insertAll(musicBrainzModels)
        areaPlaceDao.linkAreaWithPlaces(
            areaId = entityId,
            musicBrainzModels.map { place -> place.id },
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        areaPlaceDao.withTransaction {
            areaPlaceDao.deletePlacesByArea(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.PLACE)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, PlaceListItemModel> =
        areaPlaceDao.getPlacesByArea(
            areaId = entityId,
            query = "%$query%",
        )

    override fun transformDatabaseToListItemModel(databaseModel: PlaceListItemModel): PlaceListItemModel {
        return databaseModel
    }
}
