package ly.david.musicsearch.shared.feature.details.alias

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AliasListItemTest : ScreenshotTest() {

    @Test
    fun primary() {
        snapshot {
            PreviewAliasListItemPrimary()
        }
    }

    @Test
    fun ended() {
        snapshot {
            PreviewAliasListItemEnded()
        }
    }
}
