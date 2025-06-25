package ly.david.musicsearch.data.repository.series

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.SeriesListRepository

class SeriesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : SeriesListRepository,
    BrowseEntities<SeriesListItemModel, SeriesMusicBrainzNetworkModel, BrowseSeriesResponse>(
        browseEntity = MusicBrainzEntity.WORK,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeSeries(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<SeriesListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun observeCountOfAllSeries(): Flow<Int> {
        return seriesDao.observeCountOfAllSeries()
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, SeriesListItemModel> {
        return seriesDao.getSeries(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
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

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseSeriesResponse {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                browseApi.browseSeriesByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<SeriesMusicBrainzNetworkModel>,
    ) {
        seriesDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { series -> series.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int {
        return collectionEntityDao.getCountOfEntitiesByCollection(entityId)
    }
}
