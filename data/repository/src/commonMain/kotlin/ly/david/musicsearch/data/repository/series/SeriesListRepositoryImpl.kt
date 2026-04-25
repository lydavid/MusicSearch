package ly.david.musicsearch.data.repository.series

import androidx.paging.PagingData
import androidx.paging.PagingSource
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
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.series.SeriesListRepository

class SeriesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val seriesDao: SeriesDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : SeriesListRepository,
    BrowseEntities<SeriesListItemModel, SeriesMusicBrainzNetworkModel, BrowseSeriesResponse>(
        browseEntity = MusicBrainzEntityType.SERIES,
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
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entity.id,
                browseEntity = browseEntity,
            )

            when (entity.type) {
                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entity.id)
                }

                else -> error(browseEntitiesNotSupported(entity.type))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseSeriesResponse {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                browseApi.browseSeriesByCollection(
                    collectionId = entity.id,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity.type))
        }
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<SeriesMusicBrainzNetworkModel>,
    ) {
        seriesDao.upsertAll(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { series -> series.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity.type))
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return collectionEntityDao.getCountOfEntitiesByCollection(entity.id)
    }
}
