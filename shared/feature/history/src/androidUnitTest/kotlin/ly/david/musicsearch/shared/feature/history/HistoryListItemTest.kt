package ly.david.musicsearch.shared.feature.history

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class HistoryListItemTest : ScreenshotTest() {

    @Test
    fun releaseGroup() {
        snapshot {
            PreviewLookupHistoryReleaseGroup(imageUrl = "")
        }
    }

    @Test
    fun release() {
        snapshot {
            PreviewLookupHistoryRelease(imageUrl = "")
        }
    }

    @Test
    fun artist() {
        snapshot {
            PreviewLookupHistoryArtist(imageUrl = "")
        }
    }

    @Test
    fun releaseGroupWithCoverArt() {
        snapshot {
            PreviewLookupHistoryReleaseGroup()
        }
    }

    @Test
    fun releaseWithCoverArt() {
        snapshot {
            PreviewLookupHistoryRelease()
        }
    }

    @Test
    fun artistWithCoverArt() {
        snapshot {
            PreviewLookupHistoryArtist()
        }
    }
}
