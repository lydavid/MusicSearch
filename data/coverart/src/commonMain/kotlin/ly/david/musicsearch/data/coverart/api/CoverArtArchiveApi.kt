package ly.david.musicsearch.data.coverart.api

import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

interface CoverArtArchiveApi {
    /**
     * This is used to get the URLs for this [entity].
     * After calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will handle that call.
     */
    suspend fun getCoverArts(
        mbid: String,
        entity: MusicBrainzEntity,
    ): CoverArtsResponse
}
