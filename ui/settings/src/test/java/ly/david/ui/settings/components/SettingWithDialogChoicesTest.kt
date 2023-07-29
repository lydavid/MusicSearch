package ly.david.ui.settings.components

import androidx.compose.ui.res.stringResource
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.R
import ly.david.ui.settings.AppPreferences
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SettingWithDialogChoicesTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            SettingWithDialogChoices(
                titleRes = R.string.theme,
                choices = AppPreferences.Theme.values().map { stringResource(id = it.textRes) },
                selectedChoiceIndex = 0,
            )
        }
    }
}
