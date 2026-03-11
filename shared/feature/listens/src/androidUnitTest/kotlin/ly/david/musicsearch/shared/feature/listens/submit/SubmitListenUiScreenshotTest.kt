package ly.david.musicsearch.shared.feature.listens.submit

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SubmitListenUiScreenshotTest : ScreenshotTest() {

    @Test
    fun startedCustomLocal() {
        snapshot {
            PreviewSubmitListenUiStartedCustomLocal()
        }
    }

    @Test
    fun startedCustomUTC() {
        snapshot {
            PreviewSubmitListenUiStartedCustomUTC()
        }
    }

    @Test
    fun startedNowLocal() {
        snapshot {
            PreviewSubmitListenUiStartedNowLocal()
        }
    }

    @Test
    fun startedNowUTC() {
        snapshot {
            PreviewSubmitListenUiStartedNowUTC()
        }
    }

    @Test
    fun finished() {
        snapshot {
            PreviewSubmitListenUiFinished()
        }
    }

    @Test
    fun startedCustomDaylightSaving() {
        snapshot {
            PreviewSubmitListenUiStartedCustomLocalDaylightSaving()
        }
    }
}
