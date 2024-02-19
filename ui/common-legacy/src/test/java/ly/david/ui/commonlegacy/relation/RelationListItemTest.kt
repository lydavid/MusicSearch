package ly.david.ui.commonlegacy.relation

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
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
