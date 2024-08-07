package ly.david.musicsearch.data.repository.artist

import ly.david.musicsearch.core.models.artist.ArtistDetailsModel
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.internal.toRelationWithOrderList
import ly.david.musicsearch.shared.domain.artist.ArtistImageRepository
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class ArtistRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
    private val artistDao: ArtistDao,
    private val relationRepository: RelationRepository,
    private val artistImageRepository: ArtistImageRepository,
) : ArtistRepository {

    override suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel {
        if (forceRefresh) {
            relationRepository.deleteUrlRelationshipsByEntity(artistId)
            artistImageRepository.deleteImage(artistId)
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
            return artistWithUrls.copy(
                imageUrl = fetchArtistImage(artistWithUrls),
            )
        }

        val artistMusicBrainzModel = musicBrainzApi.lookupArtist(artistId)
        cache(artistMusicBrainzModel)
        return lookupArtist(
            artistId = artistId,
            forceRefresh = false,
        )
    }

    private suspend fun fetchArtistImage(
        artist: ArtistDetailsModel,
    ): String {
        val imageUrl = artist.imageUrl
        return if (imageUrl == null) {
            val spotifyUrl =
                artist.urls.firstOrNull { it.name.contains("open.spotify.com/artist/") }?.name ?: return ""
            artistImageRepository.getArtistImageFromNetwork(
                artistMbid = artist.id,
                spotifyUrl = spotifyUrl,
            )
        } else {
            imageUrl
        }
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
