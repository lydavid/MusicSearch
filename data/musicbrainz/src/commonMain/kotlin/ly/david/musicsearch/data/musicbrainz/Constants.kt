package ly.david.musicsearch.data.musicbrainz

/**
 * MusicBrainz base url for API and web.
 */
internal const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org"

/**
 * Prevent making more than 1 API call per second to not violate rate limit.
 */
const val DELAY_PAGED_API_CALLS_MS = 1000L

/**
 * Starting offset for all search/browse requests.
 */
const val STARTING_OFFSET = 0

/**
 * Limit for search/browse requests. This is the max size.
 */
const val SEARCH_BROWSE_LIMIT = 100
