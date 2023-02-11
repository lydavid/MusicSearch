package ly.david.mbjc

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import ly.david.mbjc.ui.MainActivity
import org.junit.Rule

internal abstract class MainActivityTest {
    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun waitForTextToShow(text: String) {
        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodesWithText(text)
                .fetchSemanticsNodes().size == 1
        }
    }

    fun waitThenPerformClick(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .performClick()
    }

    fun waitThenAssertIsDisplayed(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }
}
