package ly.david.mbjc.ui.navigation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.Coil
import coil.ImageLoaderFactory
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.network.resourceUri
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.test.inject

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

    private val imageLoaderFactory: ImageLoaderFactory by inject()
    private val circuit: Circuit by inject()
    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        Coil.setImageLoader(imageLoaderFactory)
        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                CircuitCompositionLocals(circuit) {
                    TopLevelScaffold(navController)
                }
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