package ly.david.data.domain.artist

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.artist.RoomArtistDao
import ly.david.data.room.artist.toArtistRoomModel
import org.koin.core.annotation.Single

@Single
class ArtistRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: RoomArtistDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artistWithAllData = artistDao.getArtist(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)
        if (artistWithAllData != null && hasUrlsBeenSavedForEntity) {
            return artistWithAllData.toArtistScaffoldModel()
        }

        val artistMusicBrainzModel = musicBrainzApi.lookupArtist(
            artistId = artistId,
        )
        artistDao.withTransaction {
            artistDao.insert(artistMusicBrainzModel.toArtistRoomModel())
            relationRepository.insertAllUrlRelations(
                entityId = artistId,
                relationMusicBrainzModels = artistMusicBrainzModel.relations,
            )
        }
        return lookupArtist(artistId)
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupArtist(
            artistId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
