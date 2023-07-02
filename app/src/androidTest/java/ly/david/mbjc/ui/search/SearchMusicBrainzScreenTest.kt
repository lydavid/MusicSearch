package ly.david.mbjc.ui.search

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.resourceUri
import ly.david.data.network.toFakeMusicBrainzModel
import ly.david.data.network.underPressureReleaseGroup
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * General UI test for search screen. For testing each resource, see [SearchEachResourceTest].
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class SearchMusicBrainzScreenTest : MainActivityTestWithMockServer(), StringReferences {

    private lateinit var navController: NavHostController

    @Before
    override fun setupApp() {
        super.setupApp()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                TopLevelScaffold(navController)
            }
        }
    }

    // TODO: sometimes fails, seems to be because of Espresso
//    @Test
//    fun searchWithEmptyText_thenBack() {
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(searchLabel)
//            .assert(hasText(""))
//            .performImeAction()
//
//        composeTestRule
//            .onNode(isDialog())
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(emptySearchWarning)
//            .assertIsDisplayed()
//
//        Espresso.pressBack()
//
//        composeTestRule
//            .onAllNodes(isDialog())
//            .assertCountEquals(0)
//    }

    @Test
    fun enterSearchText_thenClear() = runTest {
        composeTestRule.awaitIdle()

        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
            .onNodeWithTag("searchTestField")

        searchFieldNode
            .assert(hasText(""))
            .performTextInput("Hello there")

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .assertDoesNotExist()

        searchFieldNode
            .assert(hasText(""))
    }

    @Test
    fun searchHistorySmokeTest() = runTest(timeout = 15.seconds) {
        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
            .onNodeWithTag("searchTestField")

        searchFieldNode.performTextInput("Some search text")
        waitForThenPerformClickOn(MusicBrainzResource.ARTIST.toFakeMusicBrainzModel().name!!)
        composeTestRule
            .onNodeWithText(MusicBrainzResource.ARTIST.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(back)
            .performClick()
        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .performClick()

        searchFieldNode.performTextInput("Some other search text")
        waitForThenPerformClickOn(MusicBrainzResource.ARTIST.toFakeMusicBrainzModel().name!!)
        composeTestRule
            .onNodeWithText(MusicBrainzResource.ARTIST.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(back)
            .performClick()
        composeTestRule
            .onNodeWithContentDescription(clearSearchContentDescription)
            .performClick()

        // Search query shows up in search history
        composeTestRule
            .onNodeWithText("Some search text")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Some other search text")
            .assertIsDisplayed()

        // Can delete single search history
        composeTestRule
            .onNodeWithText("Some search text")
            .performTouchInput { swipeRight(startX = 0f, endX = 500f) }
        composeTestRule
            .onNodeWithText("Some search text")
            .assertIsNotDisplayedOrDoesNotExist()

        // Can delete all search history
        composeTestRule
            .onNodeWithContentDescription(clearSearchHistory)
            .performClick()
        composeTestRule
            .onNodeWithText(no)
            .performClick()
        composeTestRule
            .onNodeWithText("Some other search text")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(clearSearchHistory)
            .performClick()
        composeTestRule.awaitIdle()
        composeTestRule
            .onNodeWithText(yes)
            .performClick()
        composeTestRule
            .onNodeWithText("Some other search text")
            .assertIsNotDisplayedOrDoesNotExist()
    }

    // TODO: flaky
    //  No compose hierarchies found in the app. Possible reasons include: (1) the Activity that calls setContent did not launch; (2) setContent was not called; (3) setContent was called before the ComposeTestRule ran. If setContent is called by the Activity, make sure the Activity is launched after the ComposeTestRule runs
    @Test
    fun deeplinkToSearchWithQueryAndResource() = runTest {
        composeTestRule.awaitIdle()

        composeTestRule.activityRule.scenario.onActivity {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                val query = "some query" // The query doesn't matter for this test since we're returning fakes.
                val resource = MusicBrainzResource.RELEASE_GROUP.resourceUri
                data = Uri.parse("$deeplinkSchema://app/lookup?query=$query&type=$resource")
            }
            it.startActivity(intent)
        }

        waitForThenAssertIsDisplayed(underPressureReleaseGroup.name)
    }
}
