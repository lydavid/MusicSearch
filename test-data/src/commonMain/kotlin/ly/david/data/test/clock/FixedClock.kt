package ly.david.data.test.clock

import kotlin.time.Clock
import kotlin.time.Instant

class FixedClock(private val now: Instant) : Clock {
    override fun now(): Instant {
        return now
    }
}
