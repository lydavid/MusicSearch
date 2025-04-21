package ly.david.musicsearch.core.logging.crash

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class NoOpCrashReporterSettings : CrashReporterSettings {
    override val showCrashReporterSettings: Boolean = false
    override val isCrashReportingEnabled: Flow<Boolean> = flowOf(false)

    override fun enableCrashReporting(enable: Boolean) {
        // No-op.
    }
}
