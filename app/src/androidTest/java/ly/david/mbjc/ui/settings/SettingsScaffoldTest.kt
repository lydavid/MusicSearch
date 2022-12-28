package ly.david.mbjc.ui.settings

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.File
import kotlinx.coroutines.runBlocking
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.After
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class SettingsScaffoldTest : MainActivityTest(), StringReferences {

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        // TODO: this actually deletes our non-test datastore too...
        File(composeTestRule.activity.filesDir, "datastore").deleteRecursively()
    }

    // Doesn't actually change the theme. Limitation of compose testing?
    @Test
    fun selectTheme() {

        composeTestRule.activity.setContent {
            PreviewTheme {
                SettingsScaffold()
            }
        }

        runBlocking {
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(system)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(theme)
            .performClick()
        composeTestRule
            .onNodeWithText(light)
            .performClick()
        composeTestRule
            .onNodeWithText(light)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(theme)
            .performClick()
        composeTestRule
            .onNodeWithText(dark)
            .performClick()
        composeTestRule
            .onNodeWithText(dark)
            .assertIsDisplayed()
    }
}
