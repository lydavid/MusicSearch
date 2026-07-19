package ly.david.musicsearch.data.repository.artist

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class ArtistsListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val artistDao: ArtistDao,
    private val browseApi: BrowseApi,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    aliasDao: AliasDao,
) : ArtistsListRepository,
    BrowseEntities<ArtistListItemModel, ArtistMusicBrainzNetworkModel, BrowseArtistsResponse>(
        browseEntity = MusicBrainzEntityType.ARTIST,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
    ) {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeArtists(
        browseMethod: BrowseMethod,
        listFilters: ListFilters.Artists,
    ): Flow<PagingData<ArtistListItemModel>> {
        return listenBrainzAuthStore.browseUsername.flatMapLatest { username ->
            observeEntities(
                browseMethod = browseMethod,
                listFilters = listFilters.copy(
                    username = username,
                ),
            )
        }
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, ArtistListItemModel> {
        return artistDao.getArtists(
            browseMethod = browseMethod,
            query = listFilters.query,
            username = (listFilters as ListFilters.Artists).username,
            sortOption = listFilters.sortOption,
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

                else -> {
                    artistDao.deleteArtistLinksByEntity(entity.id)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseArtistsResponse {
        return browseApi.browseArtistsByEntity(
            entityId = entity.id,
            entity = entity.type,
            offset = offset,
        )
    }

    override fun insertAll(
        entity: MusicBrainzEntity,
        musicBrainzModels: List<ArtistMusicBrainzNetworkModel>,
    ) {
        artistDao.upsertAll(musicBrainzModels)
        when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
                    collectionId = entity.id,
                    entityIds = musicBrainzModels.map { artist -> artist.id },
                )
            }

            else -> {
                artistDao.insertArtistsByEntity(
                    entityId = entity.id,
                    artistIds = musicBrainzModels.map { artist ->
                        artist.id
                    },
                )
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity.type) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entity.id)
            }

            else -> {
                artistDao.getCountOfArtistsByEntity(entity.id)
            }
        }
    }
}
