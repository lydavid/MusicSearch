package ly.david.musicsearch.shared.domain.common

import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import kotlin.time.Instant

fun Instant.toEpochSeconds(): Long {
    return toEpochMilliseconds() / MS_IN_SECOND
}
