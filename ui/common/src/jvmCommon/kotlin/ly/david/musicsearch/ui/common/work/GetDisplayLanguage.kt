package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.data.musicbrainz.models.core.ARTIFICIAL_LANGUAGE_CODE
import ly.david.musicsearch.shared.strings.AppStrings
import java.util.Locale

actual fun String.getDisplayLanguage(appStrings: AppStrings): String? {
    return when (this) {
        // TODO: move definition to domain
        ARTIFICIAL_LANGUAGE_CODE -> appStrings.artificialLanguage
        else -> Locale(this).displayLanguage
    }
}
