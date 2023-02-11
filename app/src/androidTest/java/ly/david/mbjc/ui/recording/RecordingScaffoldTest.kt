package ly.david.mbjc.ui.recording

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
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.fakeArtistCredit
import ly.david.data.network.fakeArtistCredit2
import ly.david.data.network.fakeRecording
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class RecordingScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var recordingRepository: RecordingRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setRecording(recordingMusicBrainzModel: RecordingMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                RecordingScaffold(recordingId = recordingMusicBrainzModel.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("RecordingScaffoldTest")
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setRecording(fakeRecording)
        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeRecording.name)
            .assertIsDisplayed()

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(fakeArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit2.name)
            .assertIsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        recordingRepository.lookupRecording(fakeRecording.id)
        setRecording(fakeRecording)
        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeRecording.name)
            .assertIsDisplayed()

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(fakeArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit2.name)
            .assertIsDisplayed()
    }

//    @Test
//    fun hasRelations() = runTest {
//        // TODO: fake recording with rel
//        setRecording(fakeRecording)
//
//        composeTestRule.awaitIdle()
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        composeTestRule.awaitIdle()
//
//        // Relations are loaded
//        composeTestRule
//            .onNodeWithText(fakeAreaWithRelation.relations?.first()?.area?.name!!)
//            .assertIsDisplayed()
//    }
}
