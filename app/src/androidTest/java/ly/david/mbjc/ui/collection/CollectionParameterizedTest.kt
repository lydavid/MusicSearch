package ly.david.mbjc.ui.collection

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoaderFactory
import kotlinx.coroutines.test.runTest
import ly.david.data.musicbrainz.CollectionMusicBrainzModel
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.navigation.goToEntityScreen
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.network.collectableEntities
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.collections.CollectionListScaffold
import ly.david.ui.collections.CollectionScaffold
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.test.inject
import kotlin.time.Duration.Companion.seconds

/**
 * Tests interacting with [CollectionListScaffold] and [CollectionScaffold].
 */
@RunWith(Parameterized::class)
internal class CollectionParameterizedTest(
    private val entity: MusicBrainzEntity,
) : MainActivityTest() {

    private val strings: AppStrings by inject()

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return collectableEntities
        }
    }

    private val collectionDao: CollectionDao by inject()
    private val imageLoaderFactory: ImageLoaderFactory by inject()
    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        Coil.setImageLoader(imageLoaderFactory)
        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                TopLevelScaffold(navController)
            }
        }
    }

    @Test
    fun onlyLocalCollections() = runTest(timeout = 15.seconds) {
        composeTestRule
            .onNodeWithText(strings.collections)
            .performClick()

        val collectionName = "local $entity collection"

        collectionDao.insertLocal(
            CollectionListItemModel(
                id = entity.name,
                name = collectionName,
                entity = entity,
                isRemote = false,
                entityCount = 0,
            )
        )

        composeTestRule
            .onNodeWithText(collectionName) // list item
            .performClick()

        composeTestRule
            .onNodeWithText(collectionName) // title
            .assertIsDisplayed()

        val entityId = entity.toFakeMusicBrainzModel().id
        navController.goToEntityScreen(entity = entity, id = entityId)

        composeTestRule
            .onNodeWithContentDescription(strings.moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.addToCollection)
            .performClick()
        composeTestRule
            .onNodeWithText(collectionName)
            .performClick()

        composeTestRule
            .onNodeWithText(strings.collections)
            .performClick()
        composeTestRule
            .onNodeWithText(collectionName)
            .performClick()

        val entityName = entity.toFakeMusicBrainzModel().name!!
        composeTestRule
            .onNodeWithText(entityName) // list item
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(strings.filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("something")
        composeTestRule
            .onNodeWithText(entityName)
            .assertDoesNotExist()
        composeTestRule
            .onNodeWithContentDescription(strings.clearFilter)
            .performClick()
        composeTestRule
            .onNodeWithText(entityName)
            .performClick()

        composeTestRule
            .onNodeWithText(entityName) // title
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(strings.back)
            .performClick()
        composeTestRule
            .onNodeWithText(entityName)
            .performTouchInput { swipeRight() }
        composeTestRule
            .onNodeWithText(entityName)
            .assertDoesNotExist()
    }

    // We're currently able to skirt around the need to fake auth state but inserting data ourselves
    // since the app saves remote collection's data
    @Test
    fun onlyRemoteCollections() = runTest {
        composeTestRule
            .onNodeWithText(strings.collections)
            .performClick()

        val name = "remote $entity collection"

        collectionDao.insertRemote(
            CollectionMusicBrainzModel(
                id = entity.name,
                name = name,
                entity = entity,
            )
        )

        // Name is in list item
        waitForThenAssertIsDisplayed(name)
        waitForThenPerformClickOn(name)

        // Name is in title bar
        waitForThenAssertIsDisplayed(name)
    }
}
