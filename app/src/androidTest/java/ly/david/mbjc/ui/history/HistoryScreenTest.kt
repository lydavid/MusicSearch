package ly.david.mbjc.ui.history

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import ly.david.data.navigation.Destination
import ly.david.data.network.lookupHistory
import ly.david.data.persistence.MusicBrainzDatabase
import ly.david.data.persistence.history.LookupHistoryDao
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class HistoryScreenTest : MainActivityTest(), StringReferences {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var db: MusicBrainzDatabase
    private lateinit var lookupHistoryDao: LookupHistoryDao

    @Before
    fun setupApp() {
        hiltRule.inject()
        lookupHistoryDao = db.getLookupHistoryDao()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun emptyLookupHistory() {
        runBlocking {
            withContext(Dispatchers.Main) {
                composeTestRule.awaitIdle()
                navController.navigate(Destination.HISTORY.route)
            }
        }

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(noResultsFound)
            .assertIsDisplayed()
    }

    @Test
    fun lookupHistoryWithAnItem() {
        runBlocking {
            withContext(Dispatchers.Main) {
                lookupHistoryDao.insert(lookupHistory)
                composeTestRule.awaitIdle()
                navController.navigate(Destination.HISTORY.route)
            }
        }

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(lookupHistory.title)
            .assertIsDisplayed()
    }
}
