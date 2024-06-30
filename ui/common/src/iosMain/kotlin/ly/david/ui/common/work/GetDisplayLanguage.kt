package ly.david.ui.common.work

import ly.david.musicsearch.data.musicbrainz.models.core.ARTIFICIAL_LANGUAGE_CODE
import ly.david.musicsearch.strings.AppStrings
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
