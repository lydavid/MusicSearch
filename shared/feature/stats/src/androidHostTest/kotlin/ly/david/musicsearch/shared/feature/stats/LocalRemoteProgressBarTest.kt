package ly.david.musicsearch.shared.feature.stats

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LocalRemoteProgressBarTest : ScreenshotTest() {

    @Test
    fun noRemote() {
        snapshot {
            PreviewLocalRemoteProgressBarNoRemote()
        }
    }

    @Test
    fun empty() {
        snapshot {
            PreviewLocalRemoteProgressBarEmpty()
        }
    }

    @Test
    fun half() {
        snapshot {
            PreviewLocalRemoteProgressBarHalf()
        }
    }

    @Test
    fun full() {
        snapshot {
            PreviewLocalRemoteProgressBarFull()
        }
    }

    @Test
    fun overflow() {
        snapshot {
            PreviewLocalRemoteProgressBarOverflow()
        }
    }

    @Test
    fun unknown() {
        snapshot {
            PreviewLocalRemoteProgressBarUnknown()
        }
    }
}
