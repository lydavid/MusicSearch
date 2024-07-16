package ly.david.musicsearch.shared.domain.artist

import ly.david.musicsearch.core.models.artist.ArtistDetailsModel

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistDetailsModel
}
