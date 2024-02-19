package ly.david.ui.common.relation

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RelationListItemTest : ScreenshotTest() {

    @Test
    fun artist() {
        snapshot {
            ly.david.ui.common.relation.PreviewArtistRelationListItem()
        }
    }

    @Test
    fun recording() {
        snapshot {
            ly.david.ui.common.relation.PreviewRecordingRelationListItem()
        }
    }

    @Test
    fun relation() {
        snapshot {
            ly.david.ui.common.relation.PreviewUrlRelationListItem()
        }
    }
}
