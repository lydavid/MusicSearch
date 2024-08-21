package ly.david.musicsearch.data.repository.artist

import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
) : ArtistRepository {

    override suspend fun lookupArtistDetails(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel {
        if (forceRefresh) {
            relationRepository.deleteUrlRelationshipsByEntity(artistId)
            artistDao.delete(artistId)
        }

        val artistDetailsModel = artistDao.getArtistForDetails(artistId)
        val urlRelations = relationRepository.getEntityUrlRelationships(artistId)
        val hasUrlsBeenSavedForEntity = relationRepository.hasUrlsBeenSavedFor(artistId)

        if (
            artistDetailsModel != null &&
            hasUrlsBeenSavedForEntity &&
            !forceRefresh
        ) {
            val artistWithUrls = artistDetailsModel.copy(
                urls = urlRelations,
            )
            return artistWithUrls
        }

        val artistMusicBrainzModel = musicBrainzApi.lookupArtist(artistId)
        cache(artistMusicBrainzModel)
        return lookupArtistDetails(
            artistId = artistId,
            forceRefresh = false,
        )
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
