package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.shared.domain.musicbrainz.MULTIPLE_SCRIPT_CODE
import ly.david.musicsearch.shared.strings.AppStrings
import java.util.Locale

actual fun String.getDisplayScript(appStrings: AppStrings): String? {
    return when (this) {
        MULTIPLE_SCRIPT_CODE -> appStrings.multipleScripts
        else -> Locale.forLanguageTag("und-$this").displayScript
    }
}
