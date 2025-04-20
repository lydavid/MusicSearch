package ly.david.musicsearch.data.repository.artist

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ArtistsListRepositoryImpl(
    private val browseEntityCountDao: BrowseRemoteCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val browseApi: BrowseApi,
) : ArtistsListRepository,
    BrowseEntities<ArtistListItemModel, ArtistMusicBrainzModel, BrowseArtistsResponse>(
        browseEntity = MusicBrainzEntity.ARTIST,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeArtists(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<ArtistListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun observeCountOfAllArtists(): Flow<Long> {
        return artistDao.observeCountOfAllArtists()
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ArtistListItemModel> {
        return artistDao.getArtists(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> {
                    artistDao.deleteArtistsByEntity(entityId)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseArtistsResponse {
        return browseApi.browseArtistsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ArtistMusicBrainzModel>,
    ) {
        artistDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { artist -> artist.id },
                )
            }

            else -> {
                artistDao.insertArtistsByEntity(
                    entityId = entityId,
                    artistIds = musicBrainzModels.map { artist ->
                        artist.id
                    },
                )
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                artistDao.getCountOfArtistsByEntity(entityId)
            }
        }
    }
}
