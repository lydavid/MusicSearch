package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording

interface ArtistCollaborationRepository {
    fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording>
}
