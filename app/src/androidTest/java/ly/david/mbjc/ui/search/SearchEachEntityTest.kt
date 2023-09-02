package ly.david.mbjc.ui.search

import androidx.activity.compose.setContent
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.testing.HiltAndroidTest
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.core.network.resourceUri
import ly.david.data.core.network.searchableEntities
import ly.david.data.test.toFakeMusicBrainzModel
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.TopLevelScaffold
import ly.david.ui.common.getDisplayTextRes
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

/**
 * Test interacting with each [searchableEntities] from [SearchScreen].
 */
@HiltAndroidTest
@RunWith(Parameterized::class)
internal class SearchEachEntityTest(
    private val entity: MusicBrainzEntity
) : MainActivityTest(), StringReferences {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<MusicBrainzEntity> {
            return searchableEntities
        }
    }

    private lateinit var navController: NavHostController

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
    fun searchEachEntity() {
        composeTestRule
            .onNodeWithText(resourceLabel)
            .performClick()

        composeTestRule
            .onNodeWithTag("ExposedDropdownMenu")
            .performScrollToNode(hasTestTag(entity.resourceUri))
        composeTestRule
            .onNodeWithTag(entity.resourceUri)
            .performClick()

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(entity.getDisplayTextRes()))
            .assertIsDisplayed()

        // Entity shows up in search result
        composeTestRule.onRoot().printToLog("MY TAG")

        val searchFieldNode: SemanticsNodeInteraction = composeTestRule
            .onNodeWithTag(SearchScreenTestTag.TEXT_FIELD.name)

        searchFieldNode.assert(hasText(""))
            .performTextInput("Some search text")
        waitForThenPerformClickOn(entity.toFakeMusicBrainzModel().name!!)
        composeTestRule
            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()

        // Entity shows up in history
        composeTestRule
            .onNodeWithText(history)
            .performClick()
        composeTestRule
            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()
            .performClick()
        composeTestRule
            .onNodeWithText(entity.toFakeMusicBrainzModel().name!!)
            .assertIsDisplayed()
    }
}
