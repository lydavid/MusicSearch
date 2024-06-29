package ly.david.musicsearch.domain.artist

import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
    ): ArtistScaffoldModel
}
