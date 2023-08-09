package ly.david.data.domain.artist

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.network.RelationMusicBrainzModel
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.artist.ArtistDao
import ly.david.data.room.artist.toArtistRoomModel

@Singleton
class ArtistRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artistWithAllData = artistDao.getArtist(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)
        if (artistWithAllData != null && hasUrlsBeenSavedForEntity) {
            return artistWithAllData.toArtistScaffoldModel()
        }

        val artistMusicBrainzModel = musicBrainzApiService.lookupArtist(
            artistId = artistId,
        )
        artistDao.withTransaction {
            artistDao.insert(artistMusicBrainzModel.toArtistRoomModel())
            relationRepository.insertAllRelations(
                entityId = artistId,
                relationMusicBrainzModels = artistMusicBrainzModel.relations,
            )
        }
        return lookupArtist(artistId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApiService.lookupArtist(
            artistId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
