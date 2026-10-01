package ly.david.musicsearch.share.feature.database

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class DatabaseUiScreenshotTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewDatabaseUi()
        }
    }

    @Test
    fun withCounts() {
        snapshot {
            PreviewDatabaseUiWithCounts()
        }
    }
}
