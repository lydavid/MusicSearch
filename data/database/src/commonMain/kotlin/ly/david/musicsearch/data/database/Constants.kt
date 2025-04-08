package ly.david.musicsearch.data.database

import kotlinx.datetime.Clock

const val DATABASE_FILE_NAME = "musicsearch"
const val DATABASE_FILE_FULL_NAME = "$DATABASE_FILE_NAME.db"
const val INSERTION_FAILED_DUE_TO_CONFLICT = -1L

internal val exportFileName = "${DATABASE_FILE_NAME}_${Clock.System.now().epochSeconds}.db"
