package ly.david.ui.common.relation

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class RelationListItemTest : PaparazziScreenshotTest() {

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
