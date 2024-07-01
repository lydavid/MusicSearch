package ly.david.musicsearch.shared.feature.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingSwitchChecked
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingSwitchUnchecked
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
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
}
