package ly.david.mbjc

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.ui.Destination
import ly.david.mbjc.ui.MainActivity
import ly.david.mbjc.ui.MainApp
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainAppNavigationTest {

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        composeTestRule.setContent {
            navController = rememberNavController()
            MainApp(navController)
        }
    }

    @Test
    fun openNavigationDrawer_goToHistory() {
        composeTestRule
            .onNodeWithContentDescription("Open navigation drawer")
            .performClick()

        // TODO: there has to be a way to get these strings from resources...
        composeTestRule
            .onNodeWithText("MBJC (Debug)")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Search History")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Open navigation drawer")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Recent History")
            .assertIsDisplayed()
    }

    @Test
    fun navigateToHistoryWithRoute() {
        runBlocking {
            withContext(Dispatchers.Main) {
                navController.navigate(Destination.HISTORY.route)
            }
        }

        composeTestRule
            .onNodeWithText("Recent History")
            .assertIsDisplayed()
    }
}
