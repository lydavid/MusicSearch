package ly.david.musicsearch.domain.artist

import ly.david.musicsearch.data.core.artist.ArtistScaffoldModel

interface ArtistRepository {
    suspend fun lookupArtist(artistId: String): ArtistScaffoldModel
}
