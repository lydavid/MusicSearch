package ly.david.mbjc.ui.release

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.fakeRelease
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.release.ReleaseDao
import ly.david.data.persistence.release.toReleaseRoomModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.area.AreaScaffold
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
internal class ReleaseScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var db: MusicBrainzDatabase
    private lateinit var relationDao: RelationDao
    private lateinit var releaseDao: ReleaseDao

    // TODO: need to fake CAA service

    // TODO: fake loader: https://coil-kt.github.io/coil/image_loaders/#testing

    @Before
    fun setupApp() {
        hiltRule.inject()

        relationDao = db.getRelationDao()
        releaseDao = db.getReleaseDao()
    }

    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(releaseId = releaseMusicBrainzModel.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("ReleaseScaffoldTest")
    }

    @Test
    fun firstTimeVisit() {
        setRelease(fakeRelease)
        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
    }

    @Test
    fun repeatVisit() {
        setRelease(fakeRelease)
        runBlocking {
            releaseDao.insert(fakeRelease.toReleaseRoomModel())
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
    }

//    @Test
//    fun releaseHasRelations() {
//        setRelease(fakeAreaWithRelation)
//
//        runBlocking { composeTestRule.awaitIdle() }
//
//        composeTestRule
//            .onNodeWithText(relationships)
//            .performClick()
//
//        // Relations are loaded
//        composeTestRule
//            .onNodeWithText(fakeAreaWithRelation.relations?.first()?.area?.name ?: "")
//            .assertIsDisplayed()
//    }

    @Test
    fun useCustomName() {

        val customName = "My Custom Name"

        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(
                    releaseId = fakeRelease.id,
                    titleWithDisambiguation = customName
                )
            }
        }

        runBlocking { composeTestRule.awaitIdle() }

        composeTestRule
            .onNodeWithText(fakeRelease.name)
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(customName)
            .assertIsDisplayed()
    }

    // TODO: up nav to release group

    // TODO: up nav to artist

}
