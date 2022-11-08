package ly.david.mbjc.ui.release

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import coil.Coil
import coil.ImageLoaderFactory
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.fakeArtistCredit
import ly.david.data.network.fakeArtistCredit2
import ly.david.data.network.fakeLabel
import ly.david.data.network.fakeLabel2
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.network.fakeReleaseWithRelation
import ly.david.data.repository.ReleaseRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.area.AreaScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

/**
 * This class should test anything in [AreaScaffold] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
@HiltAndroidTest
internal class ReleaseScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var releaseRepository: ReleaseRepository

    @Inject
    lateinit var imageLoaderFactory: ImageLoaderFactory

    @Before
    fun setupApp() {
        hiltRule.inject()

        Coil.setImageLoader(imageLoaderFactory)
    }

    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(releaseId = releaseMusicBrainzModel.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ReleaseScaffoldTest")
    }

    @Test
    fun firstVisit_noLocalData() {
        setRelease(fakeRelease)
        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        // If we move this first, then it will evaluate this before it actually loads
        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(details)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeLabel.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeLabel2.name)
            .assertIsDisplayed()

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        // Can't actually navigate to these since we didn't pass in onItemClick
        // TODO: test up nav click separately in a class that uses MainActivity
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
    fun repeatVisit_localData() {
        runBlocking {
            releaseRepository.getRelease(fakeRelease.id)
            setRelease(fakeRelease)
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(details)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeLabel.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeLabel2.name)
            .assertIsDisplayed()

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
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(
                    releaseId = fakeRelease.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }

    @Test
    fun releaseHasRelations() {
        setRelease(fakeReleaseWithRelation)
        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ReleaseScaffoldTest2")

        composeTestRule
            .onNodeWithText(fakeReleaseWithRelation.relations?.first()?.release?.name ?: "")
            .assertIsDisplayed()
    }

    // TODO: seems like we haven't successfully replaced the injected loader
//    @Test
//    fun firstTimeVisit_CoverArt() {
//        setRelease(fakeReleaseWithCoverArt)
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//        composeTestRule.waitUntil {
//            composeTestRule
//                .onAllNodesWithText(fakeReleaseWithCoverArt.name)
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule
//            .onNodeWithText(fakeReleaseWithCoverArt.name)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(tracks)
//            .performClick()
//        composeTestRule.waitUntil {
//            composeTestRule
//                .onAllNodesWithTag("coverArtImage")
//                .fetchSemanticsNodes().size == 1
//        }
//        composeTestRule
//            .onNodeWithTag("coverArtImage")
//            .assertIsDisplayed()
//    }

//    @Test
//    fun repeatVisit_CoverArt() {
//        setRelease(fakeReleaseWithCoverArt)
//        runBlocking {
//            releaseDao.insert(fakeReleaseWithCoverArt.toReleaseRoomModel())
//            composeTestRule.awaitIdle()
//        }
//
//        composeTestRule
//            .onNodeWithText(fakeReleaseWithCoverArt.name)
//            .assertIsDisplayed()
//    }
}
