package ly.david.musicsearch.shared.feature.details.recording

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class RecordingUiScreenshotTest : ScreenshotTest() {

    @Test
    fun details() {
        snapshot {
            PreviewRecordingUiDetails()
        }
    }

    @Test
    fun detailsVideo() {
        snapshot {
            PreviewRecordingUiDetailsVideo()
        }
    }

    @Test
    fun detailsWithListens() {
        snapshot {
            PreviewRecordingUiDetailsWithListens()
        }
    }

    @Test
    fun detailsWithZeroListens() {
        snapshot {
            PreviewRecordingUiDetailsWithZeroListens()
        }
    }

    @Test
    fun releases() {
        snapshot {
            PreviewRecordingUiReleases()
        }
    }

    @Test
    fun relationships() {
        snapshot {
            PreviewRecordingUiRelationships()
        }
    }
}
