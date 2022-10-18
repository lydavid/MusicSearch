package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.di.MusicBrainzNetworkModule
import ly.david.mbjc.ui.MainActivity
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

// TODO: install test database so that each test doesn't start with our current data
@UninstallModules(MusicBrainzNetworkModule::class)
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class NavigateWithTitleTest(private val resource: MusicBrainzResource) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzResource> {
            return MusicBrainzResource.values().filterNot { it == MusicBrainzResource.URL }
        }
    }

    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                MainApp(navController)
            }
        }
    }

    @Test
    fun navigateToEachResourceScreenWithCustomTitle() {
        val title = resource.resourceName
        runBlocking {
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                val areaId = "497eb1f1-8632-4b4e-b29a-88aa4c08ba62"
                navController.goTo(destination = resource.toDestination(), id = areaId, title = title)
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }
}
