package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.navigation.goToResource
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

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
                it in listOf(MusicBrainzResource.URL, MusicBrainzResource.COLLECTION)
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
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun lookupEachResourceError() = runTest {
        composeTestRule.awaitIdle()

        val resourceId = "error"
        navController.goToResource(entity = resource, id = resourceId)

        waitForThenAssertAtLeastOneIsDisplayed(retry)

        // TODO: in order to make retry actually work, we need to be able to fake out error
        //  rather than pass an id that results in error
    }
}
