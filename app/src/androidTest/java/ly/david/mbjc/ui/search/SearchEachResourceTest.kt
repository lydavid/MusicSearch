package ly.david.mbjc.ui.search

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.searchableResources
import ly.david.data.network.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.MainApp
import ly.david.mbjc.ui.common.getDisplayTextRes
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Test interacting with each [searchableResources] from [SearchMusicBrainzScreen].
 */
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class SearchEachResourceTest(private val resource: MusicBrainzResource) : MainActivityTest(), StringReferences {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzResource> {
            return searchableResources
        }
    }

    private lateinit var navController: NavHostController

    @Before
    fun setupApp() {
        hiltRule.inject()

        composeTestRule.activity.setContent {
            navController = rememberNavController()
            PreviewTheme {
                MainApp(navController)
            }
        }
    }

    @Test
    fun searchEachResource() {
        composeTestRule
            .onNodeWithText(resourceLabel)
            .performClick()

        composeTestRule
            .onNodeWithTag(resource.resourceName)
            .performClick()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(resource.getDisplayTextRes()))
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(searchLabel)
            .assert(hasText(""))
            .performTextInput("Random search text")
        composeTestRule
            .onNodeWithText(searchLabel)
            .performImeAction()

        composeTestRule
            .onNodeWithText(resource.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()
            .performClick()
    }
}
