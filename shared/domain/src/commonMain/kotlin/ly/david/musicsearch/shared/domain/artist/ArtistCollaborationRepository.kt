package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface ArtistCollaborationRepository {
    fun getAllCollaboratingArtistsAndEntities(
        artistId: String,
        collaborationEntityType: MusicBrainzEntity,
        query: String,
    ): List<CollaboratingArtistAndEntity>
}
