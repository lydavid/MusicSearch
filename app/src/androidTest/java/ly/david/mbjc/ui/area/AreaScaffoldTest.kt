package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.canada
import ly.david.data.network.fakePlace
import ly.david.data.network.fakeRelease
import ly.david.data.network.ontario
import ly.david.data.network.toronto
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

/**
 * This class should test anything in [AreaScaffold] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class AreaScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var relationDao: RelationDao

    @Inject
    lateinit var areaDao: AreaDao

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
        setArea(ontario)
        areaDao.insert(ontario.toAreaRoomModel())

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(ontario.name)
        waitForThenPerformClickOn(places)
        waitForThenAssertIsDisplayed(fakePlace.name)
    }

    @Test
    fun hasRelations() = runTest {
        setArea(ontario)

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()
        waitForThenAssertIsDisplayed(canada.name)
        waitForThenAssertIsDisplayed(toronto.name)

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag("filterTextField")
            .performTextInput("something such that we show no results")
        composeTestRule
            .onNodeWithText(canada.name)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithText(toronto.name)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithTag("filterTextField")
            .performTextClearance()
        composeTestRule
            .onNodeWithTag("filterTextField")
            .performTextInput("tor")
        composeTestRule
            .onNodeWithText(canada.name)
            .assertIsNotDisplayed()
        composeTestRule
            .onNodeWithText(toronto.name)
            .assertIsDisplayed()
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

    @Test
    fun showRetryButtonOnError() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(
                    areaId = "error"
                )
            }
        }

        waitForThenAssertAtLeastOneIsDisplayed(retry)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertAtLeastOneIsDisplayed(retry)

        // TODO: showing "no results"
//        waitForThenPerformClickOn(places)
//        waitForThenAssertAtLeastOneIsDisplayed(retry)
    }

    // TODO: visit, check history count is 1, visit again, go to release, return, return, check history count is 2

    // endregion

    // region Country
    @Test
    fun countryHasReleasesTab() = runTest {
        setArea(canada)

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(fakeRelease.name)
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
