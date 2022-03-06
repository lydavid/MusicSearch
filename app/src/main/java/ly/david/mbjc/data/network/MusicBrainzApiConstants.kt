package ly.david.mbjc.data.network

/**
 * MusicBrainz base url for API and web.
 */
internal const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org"

/**
 * Prevent making more than 1 API call per second to not violate rate limit.
 */
const val DELAY_PAGED_API_CALLS_MS = 1000L

/**
 * Starting offset for all search/browse types.
 */
const val STARTING_OFFSET = 0

const val SEARCH_LIMIT = 25
const val INITIAL_SEARCH_LIMIT = 50

/**
 * Max page size for browse requests.
 */
private const val MAX_BROWSE_LIMIT = 100

/**
 * Page size limit for browse requests.
 */
const val BROWSE_LIMIT = MAX_BROWSE_LIMIT
