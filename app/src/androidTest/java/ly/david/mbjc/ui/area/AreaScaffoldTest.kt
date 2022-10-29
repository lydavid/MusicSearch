package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.network.AreaMusicBrainzModel
import ly.david.data.network.fakeArea
import ly.david.data.network.fakeAreaWithRelation
import ly.david.data.network.fakeCountry
import ly.david.data.network.fakeRelease
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.toAreaRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

/**
 * This class should test anything in [AreaScaffold] that we would otherwise have to QA manually.
 *
 * However, try to refrain from testing the details of constituent composables such as its cards.
 * These should be tested in its own test class (screenshot tests). For now, previews will be enough.
 */
@HiltAndroidTest
internal class AreaScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var db: MusicBrainzDatabase
    private lateinit var relationDao: RelationDao
    private lateinit var areaDao: AreaDao

    @Before
    fun setupApp() {
        hiltRule.inject()

        // TODO: eventually we won't have access to dao directly
        //  instead provide a test module that provides fakes and methods to set up database to certain states
        relationDao = db.getRelationDao()
        areaDao = db.getAreaDao()
    }

    private fun setArea(areaMusicBrainzModel: AreaMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(areaId = areaMusicBrainzModel.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("AreaScaffoldTest")
    }

    // region General
    @Test
    fun firstTimeVisit() {
        setArea(fakeArea)
        composeTestRule
            .onNodeWithText(fakeArea.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
    }

    @Test
    fun repeatVisit() {
        setArea(fakeArea)
        runBlocking {
            areaDao.insert(fakeArea.toAreaRoomModel())
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(fakeArea.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
    }

    @Test
    fun areaHasRelations() {
        setArea(fakeAreaWithRelation)

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        // Relations are loaded
        composeTestRule
            .onNodeWithText(fakeAreaWithRelation.relations?.first()?.area?.name ?: "")
            .assertIsDisplayed()
    }

    @Test
    fun nonCountryStatsExcludesReleases() {
        setArea(fakeArea)

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(stats)
            .performClick()

        // Need to differentiate between Releases tab and header inside stats
        composeTestRule
            .onNode(hasText(releases).and(hasNoClickAction()))
            .assertDoesNotExist()
    }

    @Test
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(
                    areaId = fakeArea.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeArea.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }
    // endregion

    // region Country
    @Test
    fun countryHasReleasesTab() {
        setArea(fakeCountry)

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(releases)
            .performClick()

        // Releases are loaded
        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertIsDisplayed()
    }

    @Test
    fun countryStatsIncludesReleases() {
        setArea(fakeCountry)

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(stats)
            .performClick()

        // Need to differentiate between Releases tab and header inside stats
        composeTestRule
            .onNode(hasText(releases).and(hasNoClickAction()))
            .assertIsDisplayed()
    }
    // endregion
}
