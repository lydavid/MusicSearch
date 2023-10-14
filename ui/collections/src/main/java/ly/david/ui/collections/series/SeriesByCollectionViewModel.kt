package ly.david.ui.collections.series

import androidx.paging.PagingSource
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.core.listitem.SeriesListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.series.SeriesPagedList
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SeriesByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val browseEntityCountDao: BrowseEntityCountDao,
    pagedList: SeriesPagedList,
) : BrowseEntitiesByEntityViewModel<SeriesListItemModel, SeriesListItemModel, SeriesMusicBrainzModel, BrowseSeriesResponse>(
    byEntity = MusicBrainzEntity.SERIES,
    browseEntityCountDao = browseEntityCountDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseSeriesResponse {
        return musicBrainzApi.browseSeriesByCollection(
            collectionId = entityId,
            offset = offset,
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<SeriesMusicBrainzModel>) {
        collectionEntityDao.withTransaction {
            seriesDao.insertAll(musicBrainzModels)
            collectionEntityDao.insertAll(
                collectionId = entityId,
                entityIds = musicBrainzModels.map { series -> series.id },
            )
        }
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
    ): PagingSource<Int, SeriesListItemModel> =
        collectionEntityDao.getSeriesByCollection(
            collectionId = entityId,
            query = "%$query%"
        )

    override fun transformDatabaseToListItemModel(databaseModel: SeriesListItemModel): SeriesListItemModel {
        return databaseModel
    }
}
