package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@HiltAndroidTest
internal class ReleaseGroupScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Test
    fun showRetryButtonOnError() {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(
                    releaseGroupId = "error"
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(retry)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        composeTestRule
            .onNodeWithText(retry)
            .assertIsDisplayed()
    }
}
