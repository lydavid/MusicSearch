package ly.david.musicsearch.shared.feature.settings.appearance

import ly.david.musicsearch.shared.feature.settings.internal.appearance.PreviewAppearanceSettingsUiCustomColors
import ly.david.musicsearch.shared.feature.settings.internal.appearance.PreviewAppearanceSettingsUiNonAndroid12Plus
import ly.david.musicsearch.shared.feature.settings.internal.appearance.PreviewAppearanceSettingsUiSystemColors
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AppearanceSettingsScreenTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun systemColors() {
        snapshot {
            PreviewAppearanceSettingsUiSystemColors()
        }
    }

    @Test
    fun customColors() {
        snapshot {
            PreviewAppearanceSettingsUiCustomColors()
        }
    }

    @Test
    fun nonAndroid12Plus() {
        snapshot {
            PreviewAppearanceSettingsUiNonAndroid12Plus()
        }
    }
}
