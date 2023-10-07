package ly.david.mbjc.ui.history

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.test.runTest
import ly.david.data.test.lookupHistory
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.musicsearch.domain.Destination
import ly.david.musicsearch.domain.history.LookupHistoryRepositoryImpl
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

internal class HistoryScreenTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private lateinit var navController: NavHostController

    private val lookupHistoryRepository: LookupHistoryRepositoryImpl by inject()

    @Before
    fun setupApp() {
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
            .onNodeWithText(strings.recentHistory)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(strings.noResultsFound)
            .assertIsDisplayed()
    }

    @Test
    fun lookupHistoryWithAnItem() = runTest {
        lookupHistoryRepository.upsert(lookupHistory)
        composeTestRule.awaitIdle()
        navController.navigate(Destination.HISTORY.route)

        composeTestRule
            .onNodeWithText(strings.recentHistory)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(lookupHistory.title)
            .assertIsDisplayed()
    }
}
