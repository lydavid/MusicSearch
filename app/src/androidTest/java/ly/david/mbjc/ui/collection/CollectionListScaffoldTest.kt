package ly.david.mbjc.ui.collection

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.resourceUri
import ly.david.data.room.collection.CollectionDao
import ly.david.data.room.collection.CollectionRoomModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.collections.CollectionListScaffold
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

/**
 * Tests [CollectionListScaffold]-specific features.
 */
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
    fun createCollections() = runTest(timeout = 15.seconds) {
        composeTestRule
            .onNodeWithText(collections)
            .performClick()

        composeTestRule
            .onNodeWithContentDescription(createCollection)
            .performClick()
        composeTestRule
            .onNodeWithText(cancel)
            .performClick()

        val name1 = "My test collection"
        composeTestRule
            .onNodeWithContentDescription(createCollection)
            .performClick()
        composeTestRule
            .onNodeWithText(name)
            .performClick()
            .performTextInput(name1)
        composeTestRule
            .onNodeWithText(ok)
            .performClick()
        composeTestRule
            .onNodeWithText("My test collection")
            .assertIsDisplayed()

        val name2 = "My other test collection"
        composeTestRule
            .onNodeWithContentDescription(createCollection)
            .performClick()
        composeTestRule
            .onNodeWithText(name)
            .performClick()
            .performTextInput(name2)
        composeTestRule
            .onNodeWithText(resourceLabel)
            .performClick()
        composeTestRule
            .onNodeWithTag("ExposedDropdownMenu")
            .performScrollToNode(hasTestTag(MusicBrainzEntity.WORK.resourceUri))
        composeTestRule
            .onNodeWithTag(MusicBrainzEntity.WORK.resourceUri)
            .performClick()
        composeTestRule
            .onNodeWithText(ok)
            .performClick()
        composeTestRule
            .onNodeWithText(name1)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(name2)
            .assertIsDisplayed()
    }

    @Test
    fun filterCollections() = runTest {
        composeTestRule
            .onNodeWithText(collections)
            .performClick()

        val name1 = "should find me"
        collectionDao.insert(
            CollectionRoomModel(
                id = "1",
                name = name1,
                entity = MusicBrainzEntity.AREA,
                isRemote = false
            )
        )

        val name2 = "but not me"
        collectionDao.insert(
            CollectionRoomModel(
                id = "2",
                name = name2,
                entity = MusicBrainzEntity.RECORDING,
                isRemote = false
            )
        )

        waitForThenAssertIsDisplayed(name1)
        waitForThenAssertIsDisplayed(name2)

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("should")
        waitForThenAssertIsDisplayed(name1)
        composeTestRule
            .onNodeWithText(name2)
            .assertIsNotDisplayedOrDoesNotExist()

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("something such that we show no results")
        composeTestRule
            .onAllNodesWithText(name1)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(name2)
            .assertCountEquals(0)
    }
}
