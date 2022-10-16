package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
    fun openNavigationDrawer_goToHistory() {
        composeTestRule
            .onNodeWithContentDescription("Open navigation drawer")
            .performClick()

        // TODO: there has to be a way to get these strings from resources...
        composeTestRule
            .onNodeWithText("MBJC (Debug)")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("History")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Open navigation drawer")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Recent History")
            .assertIsDisplayed()
    }



    @Test
    fun enterSearchText_thenClear() {
        composeTestRule
            .onNodeWithText("Search")
            .assert(hasText(""))
            .performTextInput("Hello there")

        composeTestRule
            .onNodeWithContentDescription("Clear search field.")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Clear search field.")
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText("Search")
            .assert(hasText(""))
    }
}
