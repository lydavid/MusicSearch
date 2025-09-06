package ly.david.musicsearch.shared.domain

/**
 * This is modified by the publish workflow to remove the debug suffix for the production app.
 * It must also be updated in fdroiddata.
 */
const val APPLICATION_ID = "io.github.lydavid.musicsearch.debug"
const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"

const val DEFAULT_SEED_COLOR_LONG: Long = 0xFF571AFF
const val DEFAULT_SEED_COLOR_INT: Int = DEFAULT_SEED_COLOR_LONG.toInt()

const val DEFAULT_NUMBER_OF_IMAGES_PER_ROW = 4
const val DEFAULT_IMAGES_GRID_PADDING_DP = 2

const val MS_IN_SECOND = 1000

const val NUMBER_OF_LATEST_LISTENS_TO_SHOW: Long = 3
