package ly.david.musicsearch.shared.feature.details.tag

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TagDetailsBottomSheetContentTest : ScreenshotTest() {

    @Test
    fun genre() {
        snapshot {
            PreviewTagDetailsBottomSheetContentGenre()
        }
    }

    @Test
    fun tag() {
        snapshot {
            PreviewTagDetailsBottomSheetContentTag()
        }
    }

    @Test
    fun longTag() {
        snapshot {
            PreviewTagDetailsBottomSheetContentLongTag()
        }
    }

    @Test
    fun genreUpvote() {
        snapshot {
            PreviewTagDetailsBottomSheetContentGenreUpvote()
        }
    }

    @Test
    fun genreDownvote() {
        snapshot {
            PreviewTagDetailsBottomSheetContentGenreDownvote()
        }
    }
}
