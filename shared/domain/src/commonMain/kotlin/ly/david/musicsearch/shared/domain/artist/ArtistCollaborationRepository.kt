package ly.david.musicsearch.shared.domain.artist

interface ArtistCollaborationRepository {
    fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording>
}
