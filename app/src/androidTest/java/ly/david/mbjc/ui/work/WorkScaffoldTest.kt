package ly.david.mbjc.ui.work

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.fakeWork
import ly.david.data.repository.WorkRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class WorkScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var repository: WorkRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setWork(workMusicBrainzModel: WorkMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                WorkScaffold(workId = workMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() {
        setWork(fakeWork)
        runBlocking { composeTestRule.awaitIdle() }

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() {
        runBlocking {
            repository.lookupWork(fakeWork.id)
            setWork(fakeWork)
            composeTestRule.awaitIdle()
        }

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeWork.getNameWithDisambiguation())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(details)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeWork.type!!)
            .assertIsDisplayed()
        // Doesn't work cause it contains : but we shouldn't be testing for exact string here
//        composeTestRule
//            .onNodeWithText(fakeWork.attributes!!.first().type)
//            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeWork.attributes!!.first().value)
            .assertIsDisplayed()
    }

    @Test
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                WorkScaffold(
                    workId = fakeWork.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeWork.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }

//    @Test
//    fun releaseHasRelations() {
//        setWork(fakePlaceWithRelation)
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(fakePlaceWithRelation.relations?.first()?.event?.name ?: "")
//            .assertIsDisplayed()
//    }
}
