package ly.david.musicsearch.data.repository.genre

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseGenresResponse
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GenresListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val genreDao: GenreDao,
    private val musicBrainzApi: MusicBrainzApi,
    aliasDao: AliasDao,
) : GenresListRepository,
    BrowseEntities<GenreListItemModel, GenreMusicBrainzNetworkModel, BrowseGenresResponse>(
        browseEntity = MusicBrainzEntity.GENRE,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    override fun observeGenres(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, GenreListItemModel> {
        return genreDao.getGenres(
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

                else -> {
                    error(browseEntitiesNotSupported(entity))
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseGenresResponse {
        return musicBrainzApi.browseGenresByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<GenreMusicBrainzNetworkModel>,
    ) {
        genreDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { genre -> genre.id },
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
