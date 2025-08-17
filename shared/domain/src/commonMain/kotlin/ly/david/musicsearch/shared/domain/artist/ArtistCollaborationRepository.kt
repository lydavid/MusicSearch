package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface ArtistCollaborationRepository {
    fun getAllCollaboratingArtistsAndEntities(
        artistId: String,
        collaborationEntityType: MusicBrainzEntityType,
        query: String,
    ): List<CollaboratingArtistAndEntity>
}
