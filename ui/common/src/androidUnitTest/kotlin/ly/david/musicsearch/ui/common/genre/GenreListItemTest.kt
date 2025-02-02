package ly.david.musicsearch.ui.common.genre

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class GenreListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewGenreListItem()
        }
    }
}
