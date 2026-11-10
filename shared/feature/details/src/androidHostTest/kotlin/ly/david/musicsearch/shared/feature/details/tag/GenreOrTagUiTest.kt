package ly.david.musicsearch.shared.feature.details.tag

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class GenreOrTagUiTest : ScreenshotTest() {

    @Test
    fun genre() {
        snapshot {
            PreviewGenreUi()
        }
    }

    @Test
    fun tag() {
        snapshot {
            PreviewTagUi()
        }
    }
}
