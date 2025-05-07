package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistRepositoryImpl
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao

interface TestArtistRepository {
    val artistDao: ArtistDao
    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: VisitedDao
    val relationDao: RelationDao
    val areaDao: AreaDao
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao

    fun createArtistRepository(
        artistMusicBrainzModel: ArtistMusicBrainzModel,
    ): ArtistRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return ArtistRepositoryImpl(
            artistDao = artistDao,
            relationRepository = relationRepository,
            areaDao = areaDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArtist(
                    artistId: String,
                    include: String?,
                ): ArtistMusicBrainzModel {
                    return artistMusicBrainzModel
                }
            },
        )
    }
}
