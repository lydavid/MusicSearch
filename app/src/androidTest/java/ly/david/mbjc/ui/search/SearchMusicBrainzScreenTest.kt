package ly.david.mbjc.ui.search

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.setContent
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.fakeReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * General UI test for search screen. For testing each resource, see [SearchEachResourceTest].
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
internal class SearchMusicBrainzScreenTest : MainActivityTest(), StringReferences {

    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        hiltRule.inject()

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
    fun searchWithEmptyText_thenOkay() = runTest {
        composeTestRule.awaitIdle()

        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
            .onAllNodesWithText(searchLabel)
            .filterToOne(hasImeAction(ImeAction.Search))

        searchFieldNode
            .assert(hasText(""))
            .performImeAction()

        composeTestRule
            .onNode(isDialog())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(ok)
            .performClick()

        composeTestRule
            .onAllNodes(isDialog())
            .assertCountEquals(0)
    }

    @Test
    fun enterSearchText_thenClear() = runTest {
        composeTestRule.awaitIdle()

        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
            .onAllNodesWithText(searchLabel)
            .filterToOne(hasImeAction(ImeAction.Search))

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

    // TODO: flaked
    //  No compose hierarchies found in the app. Possible reasons include: (1) the Activity that calls setContent did not launch; (2) setContent was not called; (3) setContent was called before the ComposeTestRule ran. If setContent is called by the Activity, make sure the Activity is launched after the ComposeTestRule runs
    @Test
    fun deeplinkToSearchWithQueryAndResource() = runTest {
        composeTestRule.awaitIdle()

        composeTestRule.activityRule.scenario.onActivity {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                val query = "some query" // The query doesn't matter for this test since we're returning fakes.
                val resource = MusicBrainzResource.RELEASE_GROUP.resourceName
                data = Uri.parse("$deeplinkSchema://app/lookup?query=$query&type=$resource")
            }
            it.startActivity(intent)
        }

        waitForThenAssertIsDisplayed(fakeReleaseGroup.name)
    }
}
