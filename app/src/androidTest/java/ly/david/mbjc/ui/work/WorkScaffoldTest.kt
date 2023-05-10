package ly.david.mbjc.ui.work

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.network.underPressureRecording
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

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        repository.lookupWork(fakeWorkWithAllData.id)
        setWork(fakeWorkWithAllData)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeWorkWithAllData.getNameWithDisambiguation())
        waitForThenAssertIsDisplayed(fakeWorkWithAllData.type!!)

        composeTestRule
            .onNodeWithText(fakeWorkWithAllData.attributes!!.first().value)
            .assertIsDisplayed()

        waitForThenPerformClickOn(recordings)
        waitForThenAssertIsDisplayed(underPressureRecording.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(recordings).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    // TODO: why does this take so long on CI? ~20s. is relations query really inefficient?
    @Test
    fun hasRelations() = runTest {
        setWork(fakeWorkWithAllData)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeWorkWithAllData.relations?.first()?.work?.name!!)
    }
}
