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
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

internal class NavigationTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
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
            .onNodeWithText(strings.searchMusicbrainz)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(strings.history)
            .performClick()

        composeTestRule
            .onNode(hasText(strings.searchMusicbrainz))
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(strings.recentHistory)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(strings.search)
            .performClick()

        composeTestRule
            .onNodeWithText(strings.searchMusicbrainz)
            .assertIsDisplayed()
    }

    @Test
    fun passTitleWithSpecialCharacters() = runTest {
        composeTestRule.awaitIdle()

        val title = "H&M <>#"
        val entityId = "497eb1f1-8632-4b4e-b29a-88aa4c08ba62"

        navController.goToEntityScreen(
            entity = MusicBrainzEntity.ARTIST,
            id = entityId,
            title = title
        )

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
            .onNodeWithText(strings.history)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.recentHistory)
            .assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule
            .onAllNodesWithText(strings.searchMusicbrainz)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(strings.collections)
            .performClick()
        composeTestRule
            .onAllNodesWithText(strings.collections)
            .filterToOne(hasNoClickAction())
            .assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule
            .onAllNodesWithText(strings.searchMusicbrainz)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(strings.settings)
            .performClick()
        composeTestRule
            .onAllNodesWithText(strings.settings)
            .filterToOne(hasNoClickAction())
            .assertIsDisplayed()
        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule
            .onAllNodesWithText(strings.searchMusicbrainz)
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }
}
