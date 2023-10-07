package ly.david.mbjc

import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.test.runTest
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.mbjc.ui.navigation.goToEntityScreen
import ly.david.ui.common.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.koin.test.inject

@RunWith(Parameterized::class)
internal class LookupEachEntityErrorTest(
    private val entity: MusicBrainzEntity
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

    private val strings: AppStrings by inject()
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
    fun lookupEachEntityError() = runTest {
        composeTestRule.awaitIdle()

        val entityId = "error"
        navController.goToEntityScreen(entity = entity, id = entityId)

        waitForThenAssertAtLeastOneIsDisplayed(strings.retry)

        // TODO: in order to make retry actually work, we need to be able to fake out error
        //  rather than pass an id that results in error
    }
}
