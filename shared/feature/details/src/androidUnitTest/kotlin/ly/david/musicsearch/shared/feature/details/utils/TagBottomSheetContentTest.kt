package ly.david.musicsearch.shared.feature.details.utils

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TagBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun genre() {
        snapshot {
            PreviewTagBottomSheetContentGenre()
        }
    }

    @Test
    fun tag() {
        snapshot {
            PreviewTagBottomSheetContentTag()
        }
    }

    @Test
    fun longTag() {
        snapshot {
            PreviewTagBottomSheetContentLongTag()
        }
    }
}
