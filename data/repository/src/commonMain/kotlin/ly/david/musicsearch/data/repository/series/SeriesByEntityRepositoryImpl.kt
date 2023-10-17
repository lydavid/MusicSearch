package ly.david.musicsearch.data.repository.series

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.series.SeriesByEntityRepository

class SeriesByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val musicBrainzApi: MusicBrainzApi,
) : SeriesByEntityRepository,
    BrowseEntitiesByEntity<SeriesListItemModel, SeriesMusicBrainzModel, BrowseSeriesResponse>(
        browseEntity = MusicBrainzEntity.WORK,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeSeriesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<SeriesListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, SeriesListItemModel> {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getSeriesByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseSeriesResponse {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseSeriesByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<SeriesMusicBrainzModel>,
    ) {
        seriesDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { series -> series.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
            }
        }
    }
}
