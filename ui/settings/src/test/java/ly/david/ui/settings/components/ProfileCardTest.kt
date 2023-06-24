package ly.david.ui.settings.components

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.settings.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ProfileCardTest : PaparazziScreenshotTest() {

    @Test
    fun loggedOut() {
        snapshot {
            ProfileCard(
                username = "bob",
                showLogin = false
            )
        }
    }

    @Test
    fun loggedIn() {
        snapshot {
            ProfileCard(
                username = "bob",
                showLogin = false
            )
        }
    }

    @Test
    fun loggedInWaitingForUsername() {
        snapshot {
            ProfileCard(
                username = "",
                showLogin = false
            )
        }
    }
}
