package ly.david.mbjc.ui.history

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.Destination
import ly.david.data.room.MusicSearchDatabase
import ly.david.data.room.history.RoomLookupHistoryDao
import ly.david.data.test.lookupHistory
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

internal class HistoryScreenTest : MainActivityTest(), StringReferences, KoinTest {

    private lateinit var navController: NavHostController

    private val db: MusicSearchDatabase by inject()
    private lateinit var lookupHistoryDao: RoomLookupHistoryDao

    @Before
    fun setupApp() {
        lookupHistoryDao = db.getLookupHistoryDao()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun emptyLookupHistory() = runTest {
        composeTestRule.awaitIdle()
        navController.navigate(Destination.HISTORY.route)

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(noResultsFound)
            .assertIsDisplayed()
    }

    @Test
    fun lookupHistoryWithAnItem() = runTest {
        lookupHistoryDao.insert(lookupHistory)
        composeTestRule.awaitIdle()
        navController.navigate(Destination.HISTORY.route)

        composeTestRule
            .onNodeWithText(historyScreenTitle)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(lookupHistory.title)
            .assertIsDisplayed()
    }
}
