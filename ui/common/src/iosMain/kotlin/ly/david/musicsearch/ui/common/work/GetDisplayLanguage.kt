package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.musicbrainz.ARTIFICIAL_LANGUAGE_CODE
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.artificialLanguage
import org.jetbrains.compose.resources.stringResource
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.localizedStringForLanguageCode

@Composable
actual fun String.getDisplayLanguage(): String? {
    return when (this) {
        ARTIFICIAL_LANGUAGE_CODE -> stringResource(Res.string.artificialLanguage)
        else -> {
            val locale = NSLocale.currentLocale
            locale.localizedStringForLanguageCode(this)
        }
    }
}
