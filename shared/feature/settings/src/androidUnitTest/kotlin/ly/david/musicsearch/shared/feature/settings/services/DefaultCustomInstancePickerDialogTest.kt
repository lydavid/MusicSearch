package ly.david.musicsearch.shared.feature.settings.services

import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewDefaultCustomInstancePickerDialogCustom
import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewDefaultCustomInstancePickerDialogCustomWithText
import ly.david.musicsearch.shared.feature.settings.internal.services.PreviewDefaultCustomInstancePickerDialogDefault
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class DefaultCustomInstancePickerDialogTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewDefaultCustomInstancePickerDialogDefault()
        }
    }

    @Test
    fun custom() {
        snapshot {
            PreviewDefaultCustomInstancePickerDialogCustom()
        }
    }

    @Test
    fun customWithText() {
        snapshot {
            PreviewDefaultCustomInstancePickerDialogCustomWithText()
        }
    }
}
