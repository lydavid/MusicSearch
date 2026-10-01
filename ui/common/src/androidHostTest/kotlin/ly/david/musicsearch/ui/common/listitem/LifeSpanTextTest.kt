package ly.david.musicsearch.ui.common.listitem

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LifeSpanTextTest : ScreenshotTest() {

    @Test
    fun differentBeginAndEnd() {
        snapshot {
            PreviewLifeSpanTextDifferentBeginAndEnd()
        }
    }

    @Test
    fun sameBeginAndEnd() {
        snapshot {
            PreviewLifeSpanTextSameBeginAndEnd()
        }
    }

    @Test
    fun beginOnly() {
        snapshot {
            PreviewLifeSpanTextBeginOnly()
        }
    }

    @Test
    fun endOnly() {
        snapshot {
            PreviewLifeSpanTextEndOnly()
        }
    }
}
