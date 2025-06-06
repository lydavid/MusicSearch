package ly.david.musicsearch.shared.domain.artist

import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel

interface ArtistRepository {
    suspend fun lookupArtist(
        artistId: String,
        forceRefresh: Boolean,
        lastUpdated: Instant,
    ): ArtistDetailsModel
}
