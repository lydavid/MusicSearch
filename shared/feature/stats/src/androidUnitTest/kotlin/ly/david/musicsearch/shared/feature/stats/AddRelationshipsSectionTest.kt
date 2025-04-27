package ly.david.musicsearch.shared.feature.stats

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AddRelationshipsSectionTest : ScreenshotTest() {

    @Test
    fun all() {
        snapshot {
            PreviewAddRelationshipsSectionAll()
        }
    }

    @Test
    fun noRelationships() {
        snapshot {
            PreviewAddRelationshipsSectionNoRelationships()
        }
    }

    @Test
    fun nullRelationships() {
        snapshot {
            PreviewAddRelationshipsSectionNullRelationships()
        }
    }
}
