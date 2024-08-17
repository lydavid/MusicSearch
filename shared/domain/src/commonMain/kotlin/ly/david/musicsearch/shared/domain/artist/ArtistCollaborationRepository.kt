package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndRecording

interface ArtistCollaborationRepository {
    fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording>
}
