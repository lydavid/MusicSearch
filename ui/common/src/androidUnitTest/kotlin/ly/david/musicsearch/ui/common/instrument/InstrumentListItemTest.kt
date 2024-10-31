package ly.david.musicsearch.ui.common.instrument

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class InstrumentListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PreviewInstrumentListItemModel()
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PreviewInstrumentListItemModelAllInfo()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewInstrumentListItemModelVisited()
        }
    }
}
