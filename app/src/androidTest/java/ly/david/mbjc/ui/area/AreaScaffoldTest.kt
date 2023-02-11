package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.fakeArea
import ly.david.data.network.fakeAreaWithRelation
import ly.david.data.network.fakeCountry
import ly.david.data.network.fakePlace
import ly.david.data.network.fakeRelease
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
        setArea(fakeArea)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit() = runTest {
        setArea(fakeArea)
        areaDao.insert(fakeArea.toAreaRoomModel())

        assertFieldsDisplayed()
    }

    // TODO: flake
    private fun assertFieldsDisplayed() {
        waitThenAssertIsDisplayed(fakeArea.name)
        waitThenPerformClick(places)
        waitThenAssertIsDisplayed(fakePlace.name)
    }

    @Test
    fun hasRelations() = runTest {
        setArea(fakeAreaWithRelation)

        waitThenPerformClick(relationships)
        waitThenAssertIsDisplayed(fakeAreaWithRelation.relations?.first()?.area?.name!!)
    }

    @Test
    fun nonCountryStatsExcludesReleases() = runTest {
        setArea(fakeArea)

        waitThenPerformClick(stats)

        composeTestRule.awaitIdle()

        // Need to differentiate between Releases tab and header inside stats
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

        waitThenAssertIsDisplayed(retry)

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        composeTestRule
            .onNodeWithText(retry)
            .assertIsDisplayed()

        // TODO: showing "no results"
//        composeTestRule
//            .onNodeWithText(places)
//            .performClick()
//
//        composeTestRule
//            .onNodeWithText(retry)
//            .assertIsDisplayed()
    }

    // TODO: visit, check history count is 1, visit again, go to release, return, return, check history count is 2

    // endregion

    // region Country
    @Test
    fun countryHasReleasesTab() = runTest {
        setArea(fakeCountry)

        waitThenPerformClick(releases)
        waitThenAssertIsDisplayed(fakeRelease.name)
    }

//    @Test
//    fun countryStatsIncludesReleases() {
//        setArea(fakeCountry)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(stats)
//            .performClick()
//
//        // Need to differentiate between Releases tab and header inside stats
//        composeTestRule
//            .onNode(hasText(releases).and(hasNoClickAction()), useUnmergedTree = true)
//            .assertIsDisplayed()
//    }
    // endregion
}
