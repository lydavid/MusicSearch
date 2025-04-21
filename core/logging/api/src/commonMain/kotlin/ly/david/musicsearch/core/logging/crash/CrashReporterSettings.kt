package ly.david.musicsearch.core.logging.crash

import kotlinx.coroutines.flow.Flow

interface CrashReporterSettings {
    val showCrashReporterSettings: Boolean
    val isCrashReportingEnabled: Flow<Boolean>
    fun enableCrashReporting(enable: Boolean)
}
