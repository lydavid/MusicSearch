package ly.david.musicsearch.data.database

import kotlin.time.Clock

private const val DATABASE_FILE_NAME = "musicsearch"
internal const val DATABASE_FILE_FULL_NAME = "$DATABASE_FILE_NAME.db"
const val GROUP_CONCAT_DELIMITER = "\t"

internal val exportFileName = "${DATABASE_FILE_NAME}_${Clock.System.now().epochSeconds}.db"

internal const val UNKNOWN_LISTENS_FLAG = -1L
