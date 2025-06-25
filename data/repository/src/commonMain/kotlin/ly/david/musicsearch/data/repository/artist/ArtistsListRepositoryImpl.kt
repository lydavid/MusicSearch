package ly.david.musicsearch.data.repository.artist

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ArtistsListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : ArtistsListRepository,
    BrowseEntities<ArtistListItemModel, ArtistMusicBrainzNetworkModel, BrowseArtistsResponse>(
        browseEntity = MusicBrainzEntity.ARTIST,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
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

    override fun observeCountOfArtists(browseMethod: BrowseMethod?): Flow<Int> {
        if (browseMethod == null) return flowOf()
        return artistDao.observeCountOfArtists(browseMethod = browseMethod)
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
                    artistDao.deleteArtistLinksByEntity(entityId)
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

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ArtistMusicBrainzNetworkModel>,
    ) {
        artistDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
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
