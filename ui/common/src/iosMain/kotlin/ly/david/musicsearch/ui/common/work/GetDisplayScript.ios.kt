package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.shared.domain.musicbrainz.MULTIPLE_SCRIPT_CODE
import ly.david.musicsearch.shared.strings.AppStrings
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForScriptCode

actual fun String.getDisplayScript(appStrings: AppStrings): String? {
    return when (this) {
        MULTIPLE_SCRIPT_CODE -> appStrings.multipleScripts
        else -> NSLocale.currentLocale.localizedStringForScriptCode(this)
    }
}
