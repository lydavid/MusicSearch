package ly.david.musicsearch.shared.feature.details.release

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class TrackAdditionalActionsBottomSheetContentScreenshotTest : ScreenshotTest() {
    @Test
    fun long() {
        snapshot {
            PreviewTrackAdditionalActionsBottomSheetContentLong()
        }
    }

    @Test
    fun default() {
        snapshot {
            PreviewTrackAdditionalActionsBottomSheetContent()
        }
    }
}
