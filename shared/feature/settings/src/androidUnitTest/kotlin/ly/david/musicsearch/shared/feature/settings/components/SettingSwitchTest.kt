package ly.david.musicsearch.shared.feature.settings.components

import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingSwitchChecked
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingSwitchUnchecked
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingSwitchWithSubtitle
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class SettingSwitchTest : ScreenshotTest() {

    @Test
    fun checked() {
        snapshot {
            PreviewSettingSwitchChecked()
        }
    }

    @Test
    fun unchecked() {
        snapshot {
            PreviewSettingSwitchUnchecked()
        }
    }

    @Test
    fun withSubtitle() {
        snapshot {
            PreviewSettingSwitchWithSubtitle()
        }
    }
}
