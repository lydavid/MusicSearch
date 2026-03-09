package ly.david.musicsearch.shared.feature.listens.submit

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TimePickerFieldScreenshotTest : ScreenshotTest() {

    @Test
    fun field() {
        snapshot {
            PreviewTimePickerField()
        }
    }

    @Test
    fun dialog() {
        snapshot {
            PreviewTimePickerDialog()
        }
    }

    @Test
    fun dialogDaylightSaving() {
        snapshot {
            PreviewTimePickerDialogDaylightSaving()
        }
    }
}
