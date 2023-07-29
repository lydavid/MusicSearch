package ly.david.ui.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ProfileCardTest : PaparazziScreenshotTest() {

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
