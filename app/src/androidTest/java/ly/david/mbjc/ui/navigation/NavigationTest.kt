package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.data.network.MusicBrainzResource
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class NavigationTest : MainActivityTestWithMockServer(), StringReferences {

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
    fun clickHistory_thenClickSearch() {

        // Main title
        composeTestRule
            .onNodeWithText(searchTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(history)
            .performClick()

        composeTestRule
            .onNode(hasText(searchTitle))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(searchLabel)
            .performClick()

        composeTestRule
            .onNodeWithText(searchTitle)
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
                    entity = MusicBrainzResource.ARTIST,
                    id = resourceId,
                    title = title
                )
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }

    /**
     * Ensure we don't run into another BackHandler that eats up all of our hardware back presses.
     */
    @Test
    fun pressHardwareBackButton() {
        composeTestRule
            .onNodeWithText(history)
            .performClick()
        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule
            .onAllNodesWithText(searchTitle)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(collections)
            .performClick()
        composeTestRule
            .onAllNodesWithText(collections)
            .filterToOne(hasNoClickAction())
            .assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule
            .onAllNodesWithText(searchTitle)
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
            .onAllNodesWithText(searchTitle)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }
}
