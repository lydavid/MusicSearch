package ly.david.data.domain.artist

import ly.david.data.core.image.ImageUrlDao
import ly.david.data.domain.RelationsListRepository
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.musicbrainz.api.LookupApi
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.database.dao.ArtistDao
import org.koin.core.annotation.Single

@Single
class ArtistRepository(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: ArtistDao,
    private val imageUrlDao: ImageUrlDao,
    private val relationRepository: RelationRepository,
) : RelationsListRepository {

    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artist = artistDao.getArtist(artistId)
        val largeImageUrl = imageUrlDao.getLargeUrlForEntity(artistId)
        val urlRelations = relationRepository.getEntityUrlRelationships(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)
        if (artist != null && hasUrlsBeenSavedForEntity) {
            return artist.toArtistScaffoldModel(
                imageUrl = largeImageUrl,
                urls = urlRelations,
            )
        }

        val artistMusicBrainzModel = musicBrainzApi.lookupArtist(artistId)
        artistDao.withTransaction {
            artistDao.insert(artistMusicBrainzModel)
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
