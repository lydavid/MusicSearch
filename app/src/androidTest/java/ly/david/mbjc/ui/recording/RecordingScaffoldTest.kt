package ly.david.mbjc.ui.recording

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.network.fakeRecording
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.recording.RecordingDao
import ly.david.data.persistence.recording.toRecordingRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class RecordingScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var db: MusicBrainzDatabase
    private lateinit var relationDao: RelationDao
    private lateinit var recordingDao: RecordingDao

    @Before
    fun setupApp() {
        hiltRule.inject()
        relationDao = db.getRelationDao()
        recordingDao = db.getRecordingDao()
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
    fun firstTimeVisit() {
        setRecording(fakeRecording)
        composeTestRule
            .onNodeWithText(stats)
            .performClick()

        composeTestRule
            .onNodeWithText(fakeRecording.name)
            .assertIsDisplayed()
    }

    @Test
    fun repeatVisit() {
        setRecording(fakeRecording)
        runBlocking {
            recordingDao.insert(fakeRecording.toRecordingRoomModel())
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(stats)
            .performClick()

        composeTestRule
            .onNodeWithText(fakeRecording.name)
            .assertIsDisplayed()
    }

//    @Test
//    fun recordingHasRelations() {
//        setRecording(fakeRecording)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        // Relations are loaded
//        composeTestRule
//            .onNodeWithText(fakeAreaWithRelation.relations?.first()?.area?.name ?: "")
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
}
