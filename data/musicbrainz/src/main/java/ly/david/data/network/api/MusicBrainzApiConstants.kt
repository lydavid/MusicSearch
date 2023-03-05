package ly.david.data.network.api

/**
 * MusicBrainz base url for API and web.
 */
const val MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org"

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

const val MUSIC_BRAINZ_OAUTH_CLIENT_ID = "afBf7jhb_ms-Fjqm6VTWTvVAyuAUf_xT"

/**
 * Mobile apps embed secrets in their code so we don't have to hide this.
 */
internal const val MUSIC_BRAINZ_OAUTH_CLIENT_SECRET = "nnkNItEfufwKj0-yjgmgZVrnzrXRQBN7"
