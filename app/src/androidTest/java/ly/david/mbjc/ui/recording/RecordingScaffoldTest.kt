package ly.david.mbjc.ui.recording

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.davidBowieArtistCredit
import ly.david.data.network.fakeRecording
import ly.david.data.network.queenArtistCredit
import ly.david.data.network.underPressure
import ly.david.data.repository.RecordingRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class RecordingScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var recordingRepository: RecordingRepository

    private fun setRecording(recordingMusicBrainzModel: RecordingMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                RecordingScaffold(recordingId = recordingMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setRecording(fakeRecording)

        assertFieldsDisplayed()

    }

    @Test
    fun repeatVisit_localData() = runTest {
        recordingRepository.lookupRecording(fakeRecording.id)
        setRecording(fakeRecording)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeRecording.name)

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(davidBowieArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(queenArtistCredit.name)
            .assertIsDisplayed()

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(underPressure.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setRecording(fakeRecording)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeRecording.relations?.first()?.area?.name!!)
    }
}
