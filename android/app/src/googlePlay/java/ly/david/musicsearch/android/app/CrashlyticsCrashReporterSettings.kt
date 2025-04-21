package ly.david.musicsearch.android.app

import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.core.logging.crash.CrashReporterSettings

class CrashlyticsCrashReporterSettings : CrashReporterSettings {
    private val _isCrashReportingEnabled: MutableStateFlow<Boolean> =
        MutableStateFlow(FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled)
    override val isCrashReportingEnabled: Flow<Boolean> = _isCrashReportingEnabled
    override val showCrashReporterSettings: Boolean = true

    override fun enableCrashReporting(enable: Boolean) {
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = enable
        _isCrashReportingEnabled.value = enable
    }
}
