package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.mbjc.data.persistence.MusicBrainzRoomDatabase
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.ui.MainActivity
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.navigation.Destination
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
//@UninstallModules(DatabaseModule::class)
//@HiltAndroidTest
internal class NavigationRouteTest {

//    @get:Rule(order = 0)
//    val hiltRule: HiltAndroidRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    lateinit var navController: NavHostController

    private lateinit var db: MusicBrainzRoomDatabase
    private lateinit var areaDao: AreaDao
    private lateinit var lookupHistoryDao: LookupHistoryDao
//    private lateinit var relationDao: RelationDao

    @Before
    fun setupApp() {
//        hiltRule.inject()

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

    @Test
    fun navigateToAreaWithCustomTitle() {
        val title = "Custom Title"
        runBlocking {
//            areaDao.insert(
//                AreaRoomModel(
//                    id = "497eb1f1-8632-4b4e-b29a-88aa4c08ba62",
//                    name = "Test",
//                    disambiguation = "Yes",
//                    type = "Country",
//                    lifeSpan = null,
//                )
//            )
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                val areaId = "1"
                navController.navigate("${Destination.LOOKUP_AREA.route}/$areaId?title=$title")
            }
        }

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }
}

internal class NavigationTest {

    // val composeTestRule = createComposeRule() if we don't need activity
    //  great for testing individual UI pieces
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openNavigationDrawer_goToHistory_returnToSearch() {

        // Main title
        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .performClick()

        composeTestRule
            .onNodeWithText(getAppName())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getHistoryDrawerLabel())
            .performClick()

        // Confirm that the drawer has closed.
        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(getHistoryScreenTitle())
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(getNavDrawerIconContentDescription())
            .performClick()

        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasClickAction())
            .performClick()

        composeTestRule
            .onAllNodesWithText(getSearchDrawerLabel())
            .filterToOne(matcher = hasNoClickAction())
            .assertIsDisplayed()
    }

    @Test
    fun enterSearchText_thenClear() {
        composeTestRule
            .onNodeWithText(getSearchLabel())
            .assert(hasText(""))
            .performTextInput("Hello there")

        composeTestRule
            .onNodeWithContentDescription(getClearSearchContentDescription())
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(getClearSearchContentDescription())
            .assertDoesNotExist()

        composeTestRule
            .onNodeWithText(getSearchLabel())
            .assert(hasText(""))
    }

    private fun getSearchDrawerLabel() = composeTestRule.activity.resources.getString(R.string.search_musicbrainz)
    private fun getSearchLabel() = composeTestRule.activity.resources.getString(R.string.search)
    private fun getClearSearchContentDescription() = composeTestRule.activity.resources.getString(R.string.clear_search)
    private fun getAppName() = composeTestRule.activity.resources.getString(R.string.app_name)
    private fun getNavDrawerIconContentDescription() =
        composeTestRule.activity.resources.getString(R.string.open_nav_drawer)

    private fun getHistoryDrawerLabel() = composeTestRule.activity.resources.getString(R.string.history)
    private fun getHistoryScreenTitle() = composeTestRule.activity.resources.getString(R.string.recent_history)
}
