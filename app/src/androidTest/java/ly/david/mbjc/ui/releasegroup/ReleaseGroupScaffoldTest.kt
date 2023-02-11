package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseGroupScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Test
    fun showRetryButtonOnError() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(
                    releaseGroupId = "error"
                )
            }
        }

        waitForThenAssertIsDisplayed(retry)

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        waitForThenAssertIsDisplayed(retry)
    }
}
