package ly.david.mbjc.ui.collection

import androidx.activity.compose.setContent
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.collectableResources
import ly.david.data.persistence.collection.CollectionDao
import ly.david.data.persistence.collection.CollectionRoomModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.collections.CollectionListScaffold
import ly.david.mbjc.ui.collections.MusicBrainzCollectionScaffold
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Tests interacting with [CollectionListScaffold] and [MusicBrainzCollectionScaffold].
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class CollectionTest(
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

        val name = "local $entity collection"

        collectionDao.insert(
            CollectionRoomModel(
                id = entity.name,
                name = name,
                entity = entity,
                isRemote = false
            )
        )

        // Name is in list item
        waitForThenAssertIsDisplayed(name)
        waitForThenPerformClickOn(name)

        // Name is in title bar
        waitForThenAssertIsDisplayed(name)
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
