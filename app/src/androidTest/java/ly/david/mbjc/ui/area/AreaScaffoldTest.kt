package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.data.network.AreaMusicBrainzModel
import ly.david.mbjc.data.network.fakeAreaMusicBrainzModel
import ly.david.mbjc.data.persistence.MusicBrainzDatabase
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class AreaScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var db: MusicBrainzDatabase
    private lateinit var relationDao: RelationDao
    private lateinit var areaDao: AreaDao

    private lateinit var area: AreaMusicBrainzModel

    @Before
    fun setupApp() {
        hiltRule.inject()

        // TODO: eventually we won't have access to dao directly
        //  instead provide a test module that provides fakes and methods to set up database to certain states
        relationDao = db.getRelationDao()
        areaDao = db.getAreaDao()
    }

    private fun setArea(areaMusicBrainzModel: AreaMusicBrainzModel) {
        area = areaMusicBrainzModel
        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(areaId = area.id)
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("debugTree")
    }


    @Test
    fun firstTimeVisit() {
        setArea(fakeAreaMusicBrainzModel)
        composeTestRule
            .onNodeWithText(fakeAreaMusicBrainzModel.name)
            .assertIsDisplayed()

        // todo: don't test like this
        //  this can be handled by the card itself
//        composeTestRule
//            .onNodeWithText(fakeAreaAreaRelationship.getHeader())
//            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()
    }

    @Test
    fun repeatVisit() {
        setArea(fakeAreaMusicBrainzModel)
        runBlocking {
//            withContext(Dispatchers.Main) {
//            }
            areaDao.insert(fakeAreaMusicBrainzModel.toAreaRoomModel())
            composeTestRule.awaitIdle()
        }

        composeTestRule
            .onNodeWithText(fakeAreaMusicBrainzModel.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(stats)
            .performClick()

        // TODO: it exists as a tab and an entry in stats
        //  how useful is a test like this?
        //  we should have separate test for this stats tab anyways
//        composeTestRule
//            .onNodeWithText(relationships)
//            .assertIsDisplayed()
    }

//    @Test
//    fun hasReleases() {
//        setArea(fakeCountryAreaMusicBrainzModel)
//
//        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
//
//        runBlocking {
//            withContext(Dispatchers.Main) {
////                areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
//            }
//            composeTestRule.awaitIdle()
//        }
//
//        composeTestRule
//            .onNodeWithText(releases)
//            .performClick()
//    }
}
