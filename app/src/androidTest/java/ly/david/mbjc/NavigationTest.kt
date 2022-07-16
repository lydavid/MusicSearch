package ly.david.mbjc

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import ly.david.mbjc.ui.MainActivity
import org.junit.Rule

internal class NavigationTest {

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: NavHostController

//    @Before
//    fun setupApp() {
//        composeTestRule.setContent {
//            navController = rememberNavController()
//            PreviewTheme {
//                MainApp(navController)
//            }
//        }
//    }
//
//    @Test
//    fun openNavigationDrawer_goToHistory() {
//        composeTestRule
//            .onNodeWithContentDescription("Open navigation drawer")
//            .performClick()
//
//        // TODO: there has to be a way to get these strings from resources...
//        composeTestRule
//            .onNodeWithText("MBJC (Debug)")
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText("History")
//            .performClick()
//
//        composeTestRule
//            .onNodeWithContentDescription("Open navigation drawer")
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText("Recent History")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun navigateToHistoryWithRoute() {
//        runBlocking {
//            withContext(Dispatchers.Main) {
//                navController.navigate(Destination.HISTORY.route)
//            }
//        }
//
//        composeTestRule
//            .onNodeWithText("Recent History")
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun enterSearchText_thenClear() {
//        composeTestRule
//            .onNodeWithText("Search")
//            .assert(hasText(""))
//            .performTextInput("Hello there")
//
//        composeTestRule
//            .onNodeWithContentDescription("Clear search field.")
//            .assertIsDisplayed()
//            .performClick()
//
//        composeTestRule
//            .onNodeWithContentDescription("Clear search field.")
//            .assertDoesNotExist()
//
//        composeTestRule
//            .onNodeWithText("Search")
//            .assert(hasText(""))
//    }
}
