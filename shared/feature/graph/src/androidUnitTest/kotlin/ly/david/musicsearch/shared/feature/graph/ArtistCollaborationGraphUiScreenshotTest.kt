package ly.david.musicsearch.shared.feature.graph

import androidx.compose.ui.test.junit4.createComposeRule
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ArtistCollaborationGraphUiScreenshotTest : ScreenshotTest() {

    private val composeTestRule = createComposeRule()

    @Test
    fun default() {
        snapshot {
            composeTestRule.mainClock.autoAdvance = false
            composeTestRule.mainClock.advanceTimeByFrame()
            composeTestRule.mainClock.advanceTimeBy(1000L)
            PreviewArtistCollaborationGraphUi()
        }
    }
}
