package ly.david.musicsearch.data.repository.artist

import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
) : ArtistRepository {

    override suspend fun lookupArtist(artistId: String): ArtistScaffoldModel {
        val artistScaffoldModel = artistDao.getArtistForDetails(artistId)
        val urlRelations = relationRepository.getEntityUrlRelationships(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)
        if (artistScaffoldModel != null && hasUrlsBeenSavedForEntity) {
            return artistScaffoldModel.copy(
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

            val relationWithOrderList = artist.relations.toRelationWithOrderList(artist.id)
            relationRepository.insertAllUrlRelations(
                entityId = artist.id,
                relationWithOrderList = relationWithOrderList,
            )
        }
    }
}
