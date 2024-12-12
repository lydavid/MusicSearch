package ly.david.musicsearch.ui.common.relation

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class RelationListItemTest : ScreenshotTest() {

    @Test
    fun artist() {
        snapshot {
            PreviewArtistRelationListItem()
        }
    }

    @Test
    fun recording() {
        snapshot {
            PreviewRecordingRelationListItem()
        }
    }

    @Test
    fun recordingVisited() {
        snapshot {
            PreviewRecordingRelationListItemVisited()
        }
    }
}
