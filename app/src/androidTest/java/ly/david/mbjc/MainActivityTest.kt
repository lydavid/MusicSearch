package ly.david.mbjc

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
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

    private fun waitForNodeToShow(matcher: SemanticsMatcher) {
        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodes(matcher)
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun waitForTextToShow(text: String) {
        waitForNodeToShow(hasText(text))
    }

    fun waitForThenPerformClickOn(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .performClick()
    }

    fun waitForThenAssertIsDisplayed(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onNodeWithText(text)
            .assertIsDisplayed()
    }

    fun waitForThenAssertIsDisplayed(matcher: SemanticsMatcher) {
        waitForNodeToShow(matcher)

        composeTestRule
            .onNode(matcher)
            .assertIsDisplayed()
    }
}
