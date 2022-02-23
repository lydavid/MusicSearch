package ly.david.musicbrainzjetpackcompose.preferences

// region MusicBrainz API

/**
 * Prevent making more than 1 API call per second to not violate rate limit.
 */
const val DELAY_PAGED_API_CALLS_MS = 1000L

/**
 * Max limit for browse requests.
 */
const val MAX_BROWSE_LIMIT = 100

const val STARTING_OFFSET = 0
const val SEARCH_LIMIT = 25
const val INITIAL_SEARCH_LIMIT = 50

const val NO_TYPE = "Unspecified type"
// endregion
