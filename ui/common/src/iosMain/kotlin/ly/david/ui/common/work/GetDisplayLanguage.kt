package ly.david.ui.common.work

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

actual fun String.getDisplayLanguage(): String? {
    val locale = NSLocale.currentLocale
    return locale.localizedStringForLanguageCode(this)
}
