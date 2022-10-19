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
import ly.david.mbjc.StringReferences
import org.junit.Test

@HiltAndroidTest
internal class NavigationTest : MainActivityTest(), StringReferences {

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
