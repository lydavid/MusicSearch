package ly.david.mbjc.ui.history

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidTest
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.data.persistence.MusicBrainzRoomDatabase
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.After
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class HistoryScreenTest : MainActivityTest() {

    lateinit var navController: NavHostController

    private lateinit var db: MusicBrainzRoomDatabase
    private lateinit var areaDao: AreaDao
    private lateinit var lookupHistoryDao: LookupHistoryDao
//    private lateinit var relationDao: RelationDao

    @Before
    fun setupApp() {
        hiltRule.inject()

        // TODO: the disadvantage of this is it will include any existing test data
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MusicBrainzRoomDatabase::class.java
        ).build()
        areaDao = db.getAreaDao()
        lookupHistoryDao = db.getLookupHistoryDao()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                MainApp(navController)
            }
        }
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun navigateToHistoryWithRoute() {

        runBlocking {
            withContext(Dispatchers.Main) {
//                lookupHistoryDao.deleteAllHistory()
                composeTestRule.awaitIdle()
                navController.navigate(Destination.HISTORY.route)
            }
        }

        composeTestRule
            .onNodeWithText("Recent History")
            .assertIsDisplayed()

//        composeTestRule
//            .onNodeWithText("No results found.")
//            .assertIsDisplayed()
    }
}
