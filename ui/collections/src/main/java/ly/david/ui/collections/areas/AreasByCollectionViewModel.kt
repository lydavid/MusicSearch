package ly.david.ui.collections.areas

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.AreaListItemModel
import ly.david.data.domain.listitem.toAreaListItemModel
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseAreasResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.ui.common.area.AreasPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import lydavidmusicsearchdatadatabase.Area
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AreasByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val areaDao: AreaDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: AreasPagedList,
) : BrowseEntitiesByEntityViewModel<Area, AreaListItemModel, AreaMusicBrainzModel, BrowseAreasResponse>(
    byEntity = MusicBrainzEntity.AREA,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseAreasResponse {
        return musicBrainzApi.browseAreasByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<AreaMusicBrainzModel>) {
        areaDao.insertAll(musicBrainzModels)
        collectionEntityDao.insertAll(
            collectionId = entityId,
            musicBrainzModels.map { area -> area.id }
        )
    }

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            browseEntityCountDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.AREA)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, Area> =
        collectionEntityDao.getAreasByCollection(
            collectionId = entityId,
            query = "%$query%"
        )

    override fun transformDatabaseToListItemModel(databaseModel: Area): AreaListItemModel {
        return databaseModel.toAreaListItemModel()
    }
}
