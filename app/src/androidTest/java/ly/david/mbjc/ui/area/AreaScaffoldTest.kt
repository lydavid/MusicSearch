package ly.david.mbjc.ui.area

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.data.network.areaMusicBrainzModel
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

    @Before
    fun setupApp() {
        hiltRule.inject()

        // TODO: eventually we won't have access to dao directly
        //  instead provide a test module that provides fakes and methods to set up database to certain states
        relationDao = db.getRelationDao()
        areaDao = db.getAreaDao()

        composeTestRule.activity.setContent {
            PreviewTheme {
                AreaScaffold(areaId = areaMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstTimeVisit() {

        composeTestRule
            .onNodeWithText(areaMusicBrainzModel.name)
            .assertIsDisplayed()
    }

    @Test
    fun repeatVisit() {
        runBlocking {
            withContext(Dispatchers.Main) {
                areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
                composeTestRule.awaitIdle()
            }
        }

        composeTestRule
            .onNodeWithText(areaMusicBrainzModel.name)
            .assertIsDisplayed()
    }
}
