package ly.david.musicsearch.data.musicbrainz

internal const val MUSIC_BRAINZ_OAUTH_SCOPE = "collection profile tag"

/**
 * Prevent making more than 1 API call per second to not violate rate limit.
 */
const val DELAY_PAGED_API_CALLS_MS = 1000L

/**
 * Limit for search/browse requests. This is the max size.
 */
const val SEARCH_BROWSE_LIMIT = 100
