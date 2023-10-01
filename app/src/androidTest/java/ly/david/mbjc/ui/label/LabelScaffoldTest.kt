package ly.david.mbjc.ui.label

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.domain.label.LabelRepository
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.test.elektra
import ly.david.data.test.elektraMusicGroup
import ly.david.data.test.underPressure
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class LabelScaffoldTest : MainActivityTest(), StringReferences {

    private val labelRepository: LabelRepository by inject()

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
