package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@HiltAndroidTest
@RunWith(Parameterized::class)
internal class NavigateToEachResourceWithTitleTest(private val resource: MusicBrainzResource) :
    MainActivityTestWithMockServer() {

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
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun navigateToEachResourceScreenWithCustomTitle() {
        val title = resource.resourceName
        runBlocking {
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                val resourceId = resource.toFakeMusicBrainzModel().id
                navController.goToResource(entity = resource, id = resourceId, title = title)
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }
}
