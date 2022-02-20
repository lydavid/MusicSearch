package ly.david.musicbrainzjetpackcompose.preferences

// region MusicBrainz API

/**
 * Prevent making more than 1 API call per second to not violate rate limit.
 */
const val DELAY_RECURSIVE_API_CALLS_MS = 1000L

/**
 * Max limit for browse requests.
 */
const val MAX_BROWSE_LIMIT = 100

const val NO_TYPE = "Unspecified type"
// endregion
