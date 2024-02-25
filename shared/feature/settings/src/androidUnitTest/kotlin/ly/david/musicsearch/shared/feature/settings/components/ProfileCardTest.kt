package ly.david.musicsearch.shared.feature.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewProfileCardLoggedIn
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewProfileCardLoggedInWaitingForUsername
import ly.david.musicsearch.shared.feature.settings.internal.components.PreviewProfileCardLoggedOut
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ProfileCardTest : ScreenshotTest() {

    @Test
    fun loggedOut() {
        snapshot {
            PreviewProfileCardLoggedOut()
        }
    }

    @Test
    fun loggedIn() {
        snapshot {
            PreviewProfileCardLoggedIn()
        }
    }

    @Test
    fun loggedInWaitingForUsername() {
        snapshot {
            PreviewProfileCardLoggedInWaitingForUsername()
        }
    }
}
