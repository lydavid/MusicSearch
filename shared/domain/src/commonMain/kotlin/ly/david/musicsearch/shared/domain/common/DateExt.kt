package ly.david.musicsearch.shared.domain.common

import kotlinx.datetime.Instant

internal const val DATE_FORMAT = "EEEE, MMMM d"
internal const val TIME_FORMAT = "hh:mm a"

// https://github.com/Kotlin/kotlinx-datetime/issues/211
expect fun Instant.getDateFormatted(): String

expect fun Instant.getTimeFormatted(): String
