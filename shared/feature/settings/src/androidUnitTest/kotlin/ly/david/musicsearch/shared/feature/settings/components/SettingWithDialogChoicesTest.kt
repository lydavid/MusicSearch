package ly.david.musicsearch.shared.feature.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewSettingWithDialogChoices
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SettingWithDialogChoicesTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            PreviewSettingWithDialogChoices()
        }
    }
}
