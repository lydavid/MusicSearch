package ly.david.mbjc.ui.work

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import kotlinx.coroutines.test.runTest
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.domain.work.WorkRepository
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.data.test.fakeWorkWithAllData
import ly.david.data.test.underPressureRecording
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class WorkScaffoldTest : MainActivityTest(), StringReferences {

    private val repository: WorkRepository by inject()

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
        waitForThenAssertIsDisplayed("Type: ${fakeWorkWithAllData.type!!}")

        val attributeType = fakeWorkWithAllData.attributes!!.first().type
        val attributeValue = fakeWorkWithAllData.attributes!!.first().value
        composeTestRule
            .onNodeWithText("$attributeType: $attributeValue")
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
