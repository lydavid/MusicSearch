package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import ly.david.data.network.fakeReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class ReleaseGroupScaffoldTest : MainActivityTest(), StringReferences {

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    @Test
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(
                    releaseGroupId = fakeReleaseGroup.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeReleaseGroup.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }
}
