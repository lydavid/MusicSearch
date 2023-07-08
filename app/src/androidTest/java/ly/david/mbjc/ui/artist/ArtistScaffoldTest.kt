package ly.david.mbjc.ui.artist

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import coil.Coil
import coil.ImageLoaderFactory
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.artist.ArtistRepository
import ly.david.data.getDisplayNames
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.bandAid
import ly.david.data.network.carlosAlomar
import ly.david.data.network.davidBowie
import ly.david.data.network.davidBowieDeezer
import ly.david.data.network.davidBowieSpotify
import ly.david.data.network.underPressure
import ly.david.data.network.underPressureReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class ArtistScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var artistRepository: ArtistRepository

    @Inject
    lateinit var imageLoaderFactory: ImageLoaderFactory

    @Before
    fun setupApp() {
        hiltRule.inject()
        Coil.setImageLoader(imageLoaderFactory)
    }

    private fun setArtist(artistMusicBrainzModel: ArtistMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistScaffold(artistId = artistMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest(timeout = 20.seconds) {
        setArtist(davidBowie)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest(timeout = 20.seconds) {
        artistRepository.lookupArtist(davidBowie.id)
        setArtist(davidBowie)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(davidBowie.getNameWithDisambiguation())
        waitForThenAssertIsDisplayed("Type: ${davidBowie.type!!}")
        waitForThenAssertIsDisplayed("Gender: ${davidBowie.gender!!}")
        waitForThenAssertIsDisplayed(davidBowieSpotify.resource)
        waitForThenAssertIsDisplayed(davidBowieDeezer.resource)
        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("spotify")
        composeTestRule
            .onNodeWithText("Type: ${davidBowie.type!!}")
            .assertIsNotDisplayedOrDoesNotExist()
        composeTestRule
            .onNodeWithText("Gender: ${davidBowie.gender!!}")
            .assertIsNotDisplayedOrDoesNotExist()
        waitForThenAssertIsDisplayed(davidBowieSpotify.resource)
        composeTestRule
            .onNodeWithText(davidBowieDeezer.resource)
            .assertIsNotDisplayedOrDoesNotExist()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_BACK.name)
            .performClick()

        waitForThenPerformClickOn(releaseGroups)
        composeTestRule
            .onNode(
                matcher = hasText(underPressureReleaseGroup.name).and(
                    hasAnySibling(hasText(underPressureReleaseGroup.artistCredits.getDisplayNames()))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(sort) // Doesn't do anything because it's not hooked up, just checking the text exists
            .performClick()

        waitForThenPerformClickOn(releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(
                    hasAnySibling(hasText(underPressure.date!!))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(showLessInfo)
            .performClick()

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releaseGroups).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setArtist(davidBowie)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(davidBowie.name)

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("something such that we show no results")
        composeTestRule
            .onAllNodesWithText(bandAid.name)
            .assertCountEquals(0)
        composeTestRule
            .onAllNodesWithText(carlosAlomar.name)
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextClearance()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("band")
        composeTestRule
            .onNodeWithText(bandAid.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(carlosAlomar.name)
            .assertIsNotDisplayedOrDoesNotExist()
    }

    @Test
    fun releaseGroupsSortedAndGrouped() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistScaffold(
                    artistId = davidBowie.id,
                    sortReleaseGroupListItems = true
                )
            }
        }

        waitForThenPerformClickOn(releaseGroups)
        waitForThenAssertIsDisplayed(underPressureReleaseGroup.primaryType!!) // Separator
        composeTestRule
            .onNode(
                matcher = hasText(underPressureReleaseGroup.name).and(
                    hasAnySibling(hasText(underPressureReleaseGroup.artistCredits.getDisplayNames()))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(unsort)
            .performClick()
    }

    @Test
    fun showLessInfoInReleaseListItem() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistScaffold(
                    artistId = davidBowie.id,
                    showMoreInfoInReleaseListItem = false
                )
            }
        }

        waitForThenPerformClickOn(releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
                useUnmergedTree = true
            )
        composeTestRule
            .onAllNodesWithText(underPressure.date!!)
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithContentDescription(moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(showMoreInfo)
            .performClick()
    }
}
