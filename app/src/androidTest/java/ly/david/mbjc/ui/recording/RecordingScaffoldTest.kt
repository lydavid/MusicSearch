package ly.david.mbjc.ui.recording

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import ly.david.data.network.fakeRecording
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class RecordingScaffoldTest : MainActivityTest(), StringReferences {

//    @Inject
//    lateinit var releaseRepository: ReleaseRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

//    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
//        composeTestRule.activity.setContent {
//            PreviewTheme {
//                ReleaseScaffold(releaseId = releaseMusicBrainzModel.id)
//            }
//        }
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ReleaseScaffoldTest")
//    }
//
//    @Test
//    fun firstVisit_noLocalData() {
//        setRelease(fakeRelease)
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//        // If we move this first, then it will evaluate this before it actually loads
//        composeTestRule
//            .onNodeWithText(fakeRelease.name)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(details)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeLabel.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeLabel2.name)
//            .assertIsDisplayed()
//
//        // Confirm that up navigation items exists
//        composeTestRule
//            .onNodeWithTag("TopBarSubtitle")
//            .performClick()
//        // Can't actually navigate to these since we didn't pass in onItemClick
//        // TODO: test up nav click separately in a class that uses MainActivity
//        composeTestRule
//            .onNodeWithText(fakeReleaseGroup.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeArtistCredit.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeArtistCredit2.name)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun repeatVisit_localData() {
//        runBlocking {
//            releaseRepository.getRelease(fakeRelease.id)
//            setRelease(fakeRelease)
//            composeTestRule.awaitIdle()
//        }
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeRelease.name)
//            .assertIsDisplayed()
//
//        composeTestRule
//            .onNodeWithText(details)
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeLabel.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeLabel2.name)
//            .assertIsDisplayed()
//
//        // Confirm that up navigation items exists
//        composeTestRule
//            .onNodeWithTag("TopBarSubtitle")
//            .performClick()
//        composeTestRule
//            .onNodeWithText(fakeReleaseGroup.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeArtistCredit.name)
//            .assertIsDisplayed()
//        composeTestRule
//            .onNodeWithText(fakeArtistCredit2.name)
//            .assertIsDisplayed()
//    }

    @Test
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                RecordingScaffold(
                    recordingId = fakeRecording.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeRecording.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }

//    @Test
//    fun releaseHasRelations() {
//        setRelease(fakeReleaseWithRelation)
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ReleaseScaffoldTest2")
//
//        composeTestRule
//            .onNodeWithText(fakeReleaseWithRelation.relations?.first()?.release?.name ?: "")
//            .assertIsDisplayed()
//    }
}
