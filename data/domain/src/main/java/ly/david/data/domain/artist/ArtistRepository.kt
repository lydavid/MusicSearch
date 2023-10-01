package ly.david.data.domain.artist

import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistDao
import org.koin.core.annotation.Single

@Single
class ArtistRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artistForDetails = artistDao.getArtistForDetails(artistId)
        val urlRelations = relationRepository.getEntityUrlRelationships(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)
        if (artistForDetails != null && hasUrlsBeenSavedForEntity) {
            return artistForDetails.toArtistScaffoldModel(
                urls = urlRelations,
            )
        }

        val artistMusicBrainzModel = musicBrainzApi.lookupArtist(artistId)
        cache(artistMusicBrainzModel)
        return lookupArtist(artistId)
    }

    private fun cache(artist: ArtistMusicBrainzModel) {
        artistDao.withTransaction {
            artistDao.insert(artist)
            relationRepository.insertAllUrlRelations(
                entityId = artist.id,
                relationMusicBrainzModels = artist.relations,
            )
        }
    }

    override suspend fun lookupRelationsFromNetwork(entityId: String): List<RelationMusicBrainzModel>? {
        return musicBrainzApi.lookupArtist(
            artistId = entityId,
            include = LookupApi.INC_ALL_RELATIONS_EXCEPT_URLS,
        ).relations
    }
}
