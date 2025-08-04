package ly.david.musicsearch.shared.feature.details.alias

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AliasesSectionTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewAliasesSection()
        }
    }
}
