package ly.david.mbjc.ui.label

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.LabelMusicBrainzModel
import ly.david.data.network.elektra
import ly.david.data.network.elektraMusicGroup
import ly.david.data.network.underPressure
import ly.david.data.domain.label.LabelRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.ui.common.theme.PreviewTheme
import org.junit.Test

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
        setLabel(elektra)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        labelRepository.lookupLabel(elektra.id)
        setLabel(elektra)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(elektra.getNameWithDisambiguation())

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(underPressure.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setLabel(elektra)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(elektraMusicGroup.name)
    }
}
