package ly.david.mbjc.ui.label

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.fakeLabel
import ly.david.data.network.underPressure
import ly.david.data.repository.LabelRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class LabelScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var labelRepository: LabelRepository

    private fun setLabel(labelMusicBrainzModel: LabelMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                LabelScaffold(labelId = labelMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setLabel(fakeLabel)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        labelRepository.lookupLabel(fakeLabel.id)
        setLabel(fakeLabel)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeLabel.getNameWithDisambiguation())

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(underPressure.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setLabel(fakeLabel)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeLabel.relations?.first()?.artist?.name!!)
    }
}
