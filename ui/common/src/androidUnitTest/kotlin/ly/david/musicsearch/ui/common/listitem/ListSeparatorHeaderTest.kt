package ly.david.musicsearch.ui.common.listitem

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ListSeparatorHeaderTest : ScreenshotTest() {

    @Test
    fun listSeparatorHeader() {
        snapshot {
            PreviewListSeparatorHeader()
        }
    }

    @Test
    fun listSeparatorHeaderWithUnknownNumberOfImages() {
        snapshot {
            PreviewListSeparatorHeaderWithUnknownNumberOfImages()
        }
    }

    @Test
    fun listSeparatorHeaderWithZeroImages() {
        snapshot {
            PreviewListSeparatorHeaderWithZeroImages()
        }
    }

    @Test
    fun listSeparatorHeaderWithOneImage() {
        snapshot {
            PreviewListSeparatorHeaderWithOneImage()
        }
    }

    @Test
    fun listSeparatorHeaderWithMultipleImages() {
        snapshot {
            PreviewListSeparatorHeaderWithMultipleImages()
        }
    }
}
