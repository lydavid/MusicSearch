package ly.david.musicsearch.data.repository.artist

import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.data.database.dao.ArtistCollaborationDao
import ly.david.musicsearch.shared.domain.artist.ArtistCollaborationRepository

class ArtistCollaborationRepositoryImpl(
    private val artistCollaborationDao: ArtistCollaborationDao,
) : ArtistCollaborationRepository {
    override fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording> =
        artistCollaborationDao.getAllCollaboratingArtists(
            artistId = artistId,
            query = query,
        )
}
