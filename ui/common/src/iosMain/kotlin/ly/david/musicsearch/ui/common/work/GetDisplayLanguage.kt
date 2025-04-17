package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.shared.domain.musicbrainz.ARTIFICIAL_LANGUAGE_CODE
import ly.david.musicsearch.shared.strings.AppStrings
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

actual fun String.getDisplayLanguage(appStrings: AppStrings): String? {
    return when (this) {
        ARTIFICIAL_LANGUAGE_CODE -> appStrings.artificialLanguage
        else -> {
            val locale = NSLocale.currentLocale
            locale.localizedStringForLanguageCode(this)
        }
    }
}
