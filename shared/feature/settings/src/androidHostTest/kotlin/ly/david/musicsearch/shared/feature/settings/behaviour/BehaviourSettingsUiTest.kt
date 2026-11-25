package ly.david.musicsearch.shared.feature.settings.behaviour

import ly.david.musicsearch.shared.feature.settings.internal.behaviour.PreviewBehaviourSettingsUi
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class BehaviourSettingsUiTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewBehaviourSettingsUi()
        }
    }
}
