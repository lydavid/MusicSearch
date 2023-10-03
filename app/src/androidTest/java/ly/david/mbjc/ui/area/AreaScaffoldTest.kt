package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.test.runTest
import ly.david.data.musicbrainz.AreaMusicBrainzModel
import ly.david.data.test.canada
import ly.david.data.test.fakePlace
import ly.david.data.test.ontario
import ly.david.data.test.toronto
import ly.david.data.test.underPressure
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Test
import org.koin.test.inject

/**
 * This class should test anything in [AreaScaffold] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
internal class AreaScaffoldTest : MainActivityTest(), StringReferences {

    private val areaRepository: AreaRepository by inject()

    private fun setArea(areaMusicBrainzModel: AreaMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(areaId = areaMusicBrainzModel.id)
            }
        }
    }

    // region General
    @Test
    fun firstTimeVisit() = runTest {
        setArea(ontario)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit() = runTest {
        areaRepository.lookupArea(ontario.id)
        setArea(ontario)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        composeTestRule
            .onNodeWithText(ontario.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(places)
            .performClick()
        composeTestRule
            .onNodeWithText(fakePlace.name)
            .assertIsDisplayed()
    }

    @Test
    fun hasRelations() = runTest {
        setArea(ontario)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(canada.name)
        waitForThenAssertIsDisplayed(toronto.name)

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("something such that we show no results")
        composeTestRule
            .onAllNodesWithText(canada.name)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(toronto.name)
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextClearance()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("tor")
        waitForThenAssertIsDisplayed(toronto.name)
        composeTestRule
            .onNodeWithText(canada.name)
            .assertIsNotDisplayedOrDoesNotExist()
    }

    @Test
    fun nonCountryStatsExcludesReleases() = runTest {
        setArea(ontario)

        waitForThenPerformClickOn(stats)

        // Differentiate between Releases tab and header inside stats
        composeTestRule
            .onNode(hasText(releases).and(hasNoClickAction()))
            .assertDoesNotExist()
    }

    // TODO: visit, check history count is 1, visit again, go to release, return, return, check history count is 2

    // endregion

    // region Country
    @Test
    fun countryHasReleasesTab() = runTest {
        setArea(canada)

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(underPressure.name)
    }

    @Test
    fun countryStatsIncludesReleases() = runTest {
        setArea(canada)

        waitForThenPerformClickOn(releases)
        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
    }
    // endregion
}
