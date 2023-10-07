package ly.david.mbjc.ui.label

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import kotlinx.coroutines.test.runTest
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.musicbrainz.LabelMusicBrainzModel
import ly.david.data.test.elektra
import ly.david.data.test.elektraMusicGroup
import ly.david.data.test.underPressure
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.label.LabelRepository
import ly.david.ui.common.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

internal class LabelScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
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

        waitForThenPerformClickOn(strings.releases)
        waitForThenAssertIsDisplayed(underPressure.name)

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setLabel(elektra)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(elektraMusicGroup.name)
    }
}
