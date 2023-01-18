package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.data.navigation.toDestination
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class NavigationTest : MainActivityTest(), StringReferences {

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
    fun openNavigationDrawer_goToHistory_returnToSearch() {

        // Main title
        composeTestRule
            .onAllNodesWithText(searchDrawerLabel)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .performClick()

        composeTestRule
            .onNodeWithText(appName)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(historyDrawerLabel)
            .performClick()

        // Confirm that the drawer has closed.
        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .performClick()

        composeTestRule
            .onAllNodesWithText(searchDrawerLabel)
            .filterToOne(matcher = hasClickAction())
            .performClick()

        composeTestRule
            .onAllNodesWithText(searchDrawerLabel)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }

    @Test
    fun passTitleWithSpecialCharacters() {
        val title = "H&M <>#"
        runBlocking {
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                val resourceId = "497eb1f1-8632-4b4e-b29a-88aa4c08ba62"
                navController.goToResource(
                    destination = MusicBrainzResource.ARTIST.toDestination(),
                    id = resourceId,
                    title = title
                )
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }

    @Test
    fun openNavigationDrawer_goToSettings() {
        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .performClick()

        composeTestRule
            .onNodeWithText(settings)
            .performClick()

        // Header
        composeTestRule
            .onAllNodesWithText(settings)
            .filterToOne(hasNoClickAction())
            .assertIsDisplayed()
    }

    /**
     * Ensure we don't run into another BackHandler that eats up all of our hardware back presses.
     */
    @Test
    fun pressHardwareBackButton() {
        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(navDrawerIconContentDescription)
            .performClick()

        composeTestRule
            .onNodeWithText(historyDrawerLabel)
            .performClick()

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }

        composeTestRule
            .onAllNodesWithText(searchDrawerLabel)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(settings)
            .performClick()

        composeTestRule
            .onAllNodesWithText(settings)
            .filterToOne(hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }

        composeTestRule
            .onAllNodesWithText(searchDrawerLabel)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }
}
