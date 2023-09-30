package ly.david.mbjc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import ly.david.mbjc.ui.MainActivity
import org.junit.Rule
import org.koin.test.KoinTest

internal abstract class MainActivityTest : KoinTest {

    @get:Rule(order = 0)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    fun waitForNodeToShow(matcher: SemanticsMatcher) {
        composeTestRule.waitUntilAtLeastOneExists(matcher, 10_000L)
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

    fun waitForThenAssertAtLeastOneIsDisplayed(text: String) {
        waitForTextToShow(text)

        composeTestRule
            .onAllNodesWithText(text)
            .onFirst()
            .assertIsDisplayed()
    }

    fun waitForThenAssertIsDisplayed(matcher: SemanticsMatcher) {
        waitForNodeToShow(matcher)

        composeTestRule
            .onNode(matcher)
            .assertIsDisplayed()
    }

    fun SemanticsNodeInteraction.assertIsNotDisplayedOrDoesNotExist() {
        try {
            assertIsNotDisplayed()
        } catch (e: AssertionError) {
            assertDoesNotExist()
        }
    }
}
