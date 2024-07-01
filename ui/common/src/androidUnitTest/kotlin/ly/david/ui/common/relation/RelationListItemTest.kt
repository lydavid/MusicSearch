package ly.david.ui.common.relation

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
    fun relation() {
        snapshot {
            PreviewUrlRelationListItem()
        }
    }
}
