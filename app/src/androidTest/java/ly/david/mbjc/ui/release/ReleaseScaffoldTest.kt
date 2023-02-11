package ly.david.mbjc.ui.release

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.fakeArtistCredit
import ly.david.data.network.fakeArtistCredit2
import ly.david.data.network.fakeLabel2
import ly.david.data.network.fakeLabelInfo
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseEvent
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.network.fakeReleaseWithRelation
import ly.david.data.repository.ReleaseRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var releaseRepository: ReleaseRepository

//    @Inject
//    lateinit var imageLoaderFactory: ImageLoaderFactory

    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(releaseId = releaseMusicBrainzModel.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog(this::class.java.simpleName)
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setRelease(fakeRelease)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        releaseRepository.lookupRelease(fakeRelease.id)
        setRelease(fakeRelease)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {

        waitForThenAssertIsDisplayed(fakeRelease.name)
        composeTestRule
            .onNodeWithText(fakeLabelInfo.label!!.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeLabelInfo.catalogNumber!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeLabel2.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeReleaseEvent.area!!.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeReleaseEvent.date!!)
            .assertIsDisplayed()

        // TODO: maybe don't test like this, it's hard to reference their values
        composeTestRule
            .onNodeWithText(tracks)
            .performClick()
        waitForThenAssertIsDisplayed(fakeRelease.media!!.first().tracks!!.first().title)
        waitForThenAssertIsDisplayed(fakeRelease.media!!.first().tracks!!.last().title)
        // TODO: attempted to test filtering but apparently our listitem nodes gets duplicated afterwards...

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(fakeReleaseGroup.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit2.name)
            .assertIsDisplayed()
    }

    @Test
    fun hasRelations() = runTest {
        setRelease(fakeReleaseWithRelation)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeReleaseWithRelation.relations?.first()?.release?.name!!)
    }

    @Test
    fun showRetryButtonOnError() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(
                    releaseId = "error"
                )
            }
        }

        waitForThenAssertIsDisplayed(retry)

        composeTestRule
            .onNodeWithText(tracks)
            .performClick()

        waitForThenAssertIsDisplayed(retry)
    }

    // TODO: These only works when we use real ImageLoader...
    //  Wait for: https://github.com/coil-kt/coil/pull/1451
//    @Test
//    fun firstTimeVisit_CoverArt() {
//        setRelease(fakeReleaseWithCoverArt)
//
//        composeTestRule.waitUntil {
//            composeTestRule
//                .onAllNodesWithTag("coverArtImage")
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.waitForIdle()
//        composeTestRule
//            .onNodeWithTag("coverArtImage")
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeReleaseWithCoverArt.name)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_CoverArt() {
//        runBlocking {
//            releaseRepository.lookupRelease(fakeReleaseWithCoverArt.id)
//            setRelease(fakeReleaseWithCoverArt)
//            composeTestRule.awaitIdle()
//        }
//
//        composeTestRule.waitUntil {
//            composeTestRule
//                .onAllNodesWithTag("coverArtImage")
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule.waitForIdle()
//        composeTestRule
//            .onNodeWithTag("coverArtImage")
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeReleaseWithCoverArt.name)
//            .assertIsDisplayed()
//    }
}
