package ly.david.mbjc.ui.navigation

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.R
import org.junit.Test

@HiltAndroidTest
internal class NavigationTest : MainActivityTest() {

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
