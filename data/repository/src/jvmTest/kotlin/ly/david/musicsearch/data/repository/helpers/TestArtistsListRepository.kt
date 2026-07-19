package ly.david.musicsearch.data.repository.helpers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.data.test.NoOpListenBrainzAuthStore
import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.artist.ArtistsListRepositoryImpl
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface TestArtistsListRepository {
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao
    val collectionEntityDao: CollectionEntityDao
    val artistDao: ArtistDao
    val aliasDao: AliasDao

    fun createArtistsListRepository(
        artists: List<ArtistMusicBrainzNetworkModel>,
        fakeBrowseUsername: String = "",
    ): ArtistsListRepository {
        return ArtistsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            artistDao = artistDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseArtistsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseArtistsResponse {
                    return BrowseArtistsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = artists,
                    )
                }
            },
            listenBrainzAuthStore = object : NoOpListenBrainzAuthStore() {
                override val browseUsername: Flow<String>
                    get() = flowOf(fakeBrowseUsername)
            },
        )
    }
}
