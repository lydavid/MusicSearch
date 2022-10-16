package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.ui.MainActivity
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class NavigationRouteTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                MainApp(navController)
            }
        }
    }

    @Test
    fun navigateToHistoryWithRoute() {

        runBlocking {
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                navController.navigate(Destination.HISTORY.route)
            }
        }

        composeTestRule
            .onNodeWithText("Recent History")
            .assertIsDisplayed()
    }

//    @Test
//    fun navigateToAreaWithRoute() {
//
//        runBlocking {
//            withContext(Dispatchers.Main) {
//                composeTestRule.awaitIdle()
//                val areaId = "497eb1f1-8632-4b4e-b29a-88aa4c08ba62"
//                navController.navigate("${Destination.LOOKUP_ARTIST.route}/$areaId")
//            }
//        }
//
//        composeTestRule
//            .onNodeWithText("Recent History")
//            .assertIsDisplayed()
//    }
}

internal class NavigationTest {

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openNavigationDrawer_goToHistory_returnToSearch() {

        // Main title
        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .performClick()

        composeTestRule
            .onNodeWithText(getAppName())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getHistoryDrawerLabel())
            .performClick()

        // Confirm that the drawer has closed.
        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getHistoryScreenTitle())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .performClick()

        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasClickAction())
            .performClick()

        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }

    @Test
    fun enterSearchText_thenClear() {
        composeTestRule
            .onNodeWithText(getSearchLabel())
            .assert(hasText(""))
            .performTextInput("Hello there")

        composeTestRule
            .onNodeWithContentDescription(getClearSearchContentDescription())
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(getClearSearchContentDescription())
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(getSearchLabel())
            .assert(hasText(""))
    }

    private fun getSearchDrawerLabel() = composeTestRule.activity.resources.getString(R.string.search_musicbrainz)
    private fun getSearchLabel() = composeTestRule.activity.resources.getString(R.string.search)
    private fun getClearSearchContentDescription() = composeTestRule.activity.resources.getString(R.string.clear_search)
    private fun getAppName() = composeTestRule.activity.resources.getString(R.string.app_name)
    private fun getNavDrawerIconContentDescription() =
        composeTestRule.activity.resources.getString(R.string.open_nav_drawer)

    private fun getHistoryDrawerLabel() = composeTestRule.activity.resources.getString(R.string.history)
    private fun getHistoryScreenTitle() = composeTestRule.activity.resources.getString(R.string.recent_history)
}
