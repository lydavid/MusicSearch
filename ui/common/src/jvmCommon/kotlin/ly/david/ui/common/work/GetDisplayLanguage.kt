package ly.david.ui.common.work

import java.util.Locale

actual fun String.getDisplayLanguage(): String? {
    return Locale(this).displayLanguage
}
