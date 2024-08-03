package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.core.models.artist.ArtistDetailsModel
import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel

    fun getAllCollaboratingArtistsAndRecordings(
        artistId: String,
        query: String,
    ): List<CollaboratingArtistAndRecording>
}
