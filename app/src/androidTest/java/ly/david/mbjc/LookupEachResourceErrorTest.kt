package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import ly.david.data.navigation.toLookupDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.navigation.goToResource
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class LookupEachResourceErrorTest(
    private val resource: MusicBrainzResource
) : MainActivityTestWithMockServer(), StringReferences {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzResource> {
            return MusicBrainzResource.values().filterNot {
                it == MusicBrainzResource.URL
            }
        }
    }

    private lateinit var navController: NavHostController

    @Before
    override fun setupApp() {
        super.setupApp()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                MainApp(navController)
            }
        }
    }

    @Test
    fun lookupEachResourceError() = runTest {
        withContext(Dispatchers.Main) {
            composeTestRule.awaitIdle()
            val resourceId = "error"
            navController.goToResource(destination = resource.toLookupDestination(), id = resourceId)
        }

        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodesWithText(retry)
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithText(retry)
            .assertIsDisplayed()

        // TODO: in order to make retry actually work, we need to be able to fake out error
        //  rather than pass an id that results in error
    }
}
