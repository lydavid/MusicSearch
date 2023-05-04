package ly.david.mbjc.ui.collection

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.collectableResources
import ly.david.data.network.toFakeMusicBrainzModel
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.collections.CollectionScaffold
import ly.david.mbjc.ui.navigation.goToResource
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Tests interacting with [CollectionListScaffold] and [CollectionScaffold].
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class CollectionParameterizedTest(
    private val entity: MusicBrainzResource
) : MainActivityTest(), StringReferences {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzResource> {
            return collectableResources
        }
    }

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

        val collectionName = "local $entity collection"

        collectionDao.insert(
            CollectionRoomModel(
                id = entity.name,
                name = collectionName,
                entity = entity,
                isRemote = false
            )
        )

        composeTestRule
            .onNodeWithText(collectionName) // list item
            .performClick()

        composeTestRule
            .onNodeWithText(collectionName) // title
            .assertIsDisplayed()

        withContext(Dispatchers.Main) {
            composeTestRule.awaitIdle()
            val resourceId = entity.toFakeMusicBrainzModel().id
            navController.goToResource(entity = entity, id = resourceId)
        }
        composeTestRule
            .onNodeWithContentDescription(moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(addToCollection)
            .performClick()
        composeTestRule
            .onNodeWithText(collectionName)
            .performClick()

        composeTestRule
            .onNodeWithText(collections)
            .performClick()
        composeTestRule
            .onNodeWithText(collectionName)
            .performClick()

        val entityName = entity.toFakeMusicBrainzModel().name!!
        composeTestRule
            .onNodeWithText(entityName) // list item
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag("filterTextField")
            .performTextInput("something")
        composeTestRule
            .onNodeWithText(entityName)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(clearFilterText)
            .performClick()
        composeTestRule
            .onNodeWithText(entityName)
            .performClick()

        composeTestRule
            .onNodeWithText(entityName) // title
            .assertIsDisplayed()
    }

    // We're currently able to skirt around the need to fake auth state but inserting data ourselves
    // since the app saves remote collection's data
    @Test
    fun onlyRemoteCollections() = runTest {
        composeTestRule
            .onNodeWithText(collections)
            .performClick()

        val name = "remote $entity collection"

        collectionDao.insert(
            CollectionRoomModel(
                id = entity.name,
                name = name,
                entity = entity,
                isRemote = true
            )
        )

        // Name is in list item
        waitForThenAssertIsDisplayed(name)
        waitForThenPerformClickOn(name)

        // Name is in title bar
        waitForThenAssertIsDisplayed(name)
    }
}
