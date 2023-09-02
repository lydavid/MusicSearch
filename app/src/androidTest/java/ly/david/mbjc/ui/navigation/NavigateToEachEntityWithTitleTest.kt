package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.resourceUri
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@HiltAndroidTest
@RunWith(Parameterized::class)
internal class NavigateToEachEntityWithTitleTest(
    private val entity: MusicBrainzEntity,
) : MainActivityTest() {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return MusicBrainzEntity.values().filterNot {
                it in listOf(MusicBrainzEntity.URL, MusicBrainzEntity.COLLECTION)
            }
        }
    }

    private lateinit var navController: NavHostController

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
    fun navigateToEachEntityScreenWithCustomTitle() = runTest {
        composeTestRule.awaitIdle()

        val title = entity.resourceUri
        val entityId = entity.toFakeMusicBrainzModel().id

        navController.goToEntityScreen(entity = entity, id = entityId, title = title)

        composeTestRule
            .onNodeWithText(title)
            .assertIsDisplayed()
    }
}
