package ly.david.musicsearch.data.repository.artist

import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.data.database.dao.ArtistCollaborationDao
import ly.david.musicsearch.shared.domain.artist.ArtistCollaborationRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class ArtistCollaborationRepositoryImpl(
    private val artistCollaborationDao: ArtistCollaborationDao,
) : ArtistCollaborationRepository {
    override fun getAllCollaboratingArtistsAndEntities(
        artistId: String,
        collaborationEntityType: MusicBrainzEntity,
        query: String,
    ): List<CollaboratingArtistAndEntity> =
        artistCollaborationDao.getAllCollaboratingArtistsAndEntities(
            artistId = artistId,
            collaborationEntityType = collaborationEntityType,
            query = query,
        )
}
