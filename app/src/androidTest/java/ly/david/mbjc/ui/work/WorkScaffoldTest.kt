package ly.david.mbjc.ui.work

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.fakeWorkWithAllData
import ly.david.data.repository.WorkRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun firstVisit_noLocalData() = runTest {
        setWork(fakeWorkWithAllData)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    // TODO: flake
    @Test
    fun repeatVisit_localData() = runTest {
        repository.lookupWork(fakeWorkWithAllData.id)
        setWork(fakeWorkWithAllData)
        composeTestRule.awaitIdle()

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        composeTestRule
            .onNodeWithText(stats)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeWorkWithAllData.getNameWithDisambiguation())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(details)
            .performClick()
        composeTestRule
            .onNodeWithText(fakeWorkWithAllData.type!!)
            .assertIsDisplayed()
        // TODO: Doesn't work cause it contains : but we shouldn't be testing for exact string here
//        composeTestRule
//            .onNodeWithText(fakeWork.attributes!!.first().type)
//            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeWorkWithAllData.attributes!!.first().value)
            .assertIsDisplayed()
    }

    @Test
    fun hasRelations() = runTest {
        setWork(fakeWorkWithAllData)
        composeTestRule.awaitIdle()

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodesWithText(fakeWorkWithAllData.relations?.first()?.work?.name!!)
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule
            .onNodeWithText(fakeWorkWithAllData.relations?.first()?.work?.name!!)
            .assertIsDisplayed()
    }
}
