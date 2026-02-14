package ly.david.musicsearch.ui.common.work

import java.util.Locale

internal actual fun String.getPlatformDisplayScript(): String? {
    return Locale.forLanguageTag("und-$this").displayScript
}
