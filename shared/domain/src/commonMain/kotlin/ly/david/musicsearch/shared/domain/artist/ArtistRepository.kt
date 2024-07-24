package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.core.models.artist.ArtistDetailsModel
import ly.david.musicsearch.core.models.artist.CollaboratingArtist

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel

    fun getAllCollaboratingArtists(artistId: String): List<CollaboratingArtist>
}
