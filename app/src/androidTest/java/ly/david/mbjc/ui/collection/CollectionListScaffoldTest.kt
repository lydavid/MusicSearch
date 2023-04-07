package ly.david.mbjc.ui.collection

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

/**
 * Tests [CollectionListScaffold]-specific features.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class CollectionListScaffoldTest : MainActivityTest(), StringReferences {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var collectionDao: CollectionDao

    @Before
    fun setupApp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun onlyLocalCollections() = runTest {
        composeTestRule
            .onNodeWithText(collections)
            .performClick()

        val name1 = "should find me"
        collectionDao.insert(
            CollectionRoomModel(
                id = "1",
                name = name1,
                entity = MusicBrainzResource.AREA,
                isRemote = false
            )
        )

        val name2 = "but not me"
        collectionDao.insert(
            CollectionRoomModel(
                id = "2",
                name = name2,
                entity = MusicBrainzResource.RECORDING,
                isRemote = false
            )
        )

        composeTestRule
            .onNodeWithText(name1)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(name2)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag("filterTextField")
            .performTextInput("should")

        composeTestRule
            .onNodeWithText(name1)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(name2)
            .assertIsNotDisplayed()
    }
}
