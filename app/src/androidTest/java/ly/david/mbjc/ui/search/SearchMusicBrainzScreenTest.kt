package ly.david.mbjc.ui.search

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.espresso.Espresso
import dagger.hilt.android.testing.HiltAndroidTest
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

/**
 * General UI test for search screen. For testing each resource, see [SearchEachResourceTest].
 */
@HiltAndroidTest
internal class SearchMusicBrainzScreenTest : MainActivityTest(), StringReferences {

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
    fun searchWithEmptyText_thenBack() {
        composeTestRule
            .onNodeWithText(searchLabel)
            .assert(hasText(""))
            .performImeAction()

        composeTestRule
            .onNode(isDialog())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(emptySearchWarning)
            .assertIsDisplayed()

        Espresso.pressBack()

        composeTestRule
            .onAllNodes(isDialog())
            .assertCountEquals(0)
    }

    @Test
    fun searchWithEmptyText_thenOkay() {
        composeTestRule
            .onNodeWithText(searchLabel)
            .assert(hasText(""))
            .performImeAction()

        composeTestRule
            .onNode(isDialog())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(ok)
            .performClick()

        composeTestRule
            .onAllNodes(isDialog())
            .assertCountEquals(0)
    }

    @Test
    fun enterSearchText_thenClear() {
        composeTestRule
            .onNodeWithText(searchLabel)
            .assert(hasText(""))
            .performTextInput("Hello there")

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(searchLabel)
            .assert(hasText(""))
    }
}
