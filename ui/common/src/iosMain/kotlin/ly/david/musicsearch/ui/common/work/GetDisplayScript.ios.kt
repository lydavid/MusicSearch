package ly.david.musicsearch.ui.common.work

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForScriptCode

internal actual fun String.getPlatformDisplayScript(): String? {
    return NSLocale.currentLocale.localizedStringForScriptCode(this)
}
