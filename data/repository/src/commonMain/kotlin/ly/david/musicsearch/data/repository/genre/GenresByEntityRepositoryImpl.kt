package ly.david.musicsearch.data.repository.genre

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseGenresResponse
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class GenresByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val genreDao: GenreDao,
    private val musicBrainzApi: MusicBrainzApi,
) : GenresByEntityRepository,
    BrowseEntitiesByEntity<GenreListItemModel, GenreMusicBrainzModel, BrowseGenresResponse>(
        browseEntity = MusicBrainzEntity.GENRE,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeGenresByEntity(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<GenreListItemModel>> {
        return observeEntitiesByEntity(
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

                else -> {
                    error(browseEntitiesNotSupported(entity))
                }
            }
        }
    }

    override suspend fun browseEntities(
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

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<GenreMusicBrainzModel>,
    ): Int {
        genreDao.insertAll(musicBrainzModels)
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
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
