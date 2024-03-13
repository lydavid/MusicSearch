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
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.data.test.bandAid
import ly.david.data.test.carlosAlomar
import ly.david.data.test.davidBowie
import ly.david.data.test.davidBowieDeezer
import ly.david.data.test.davidBowieSpotify
import ly.david.data.test.underPressure
import ly.david.data.test.underPressureReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.feature.details.artist.ArtistUi
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.koin.test.inject
import kotlin.time.Duration.Companion.seconds

internal class ArtistScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val artistRepository: ArtistRepository by inject()
    private val imageLoaderFactory: ImageLoaderFactory by inject()

    @Before
    fun setupApp() {
        Coil.setImageLoader(imageLoaderFactory)
    }

    private fun setArtist(artistMusicBrainzModel: ArtistMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistUi(artistId = artistMusicBrainzModel.id)
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
            .onNodeWithContentDescription(strings.filter)
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

        waitForThenPerformClickOn(strings.releaseGroups)
        composeTestRule
            .onNode(
                matcher = hasText(underPressureReleaseGroup.name).and(
                    hasAnySibling(hasText(underPressureReleaseGroup.artistCredits.getDisplayNames()))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(strings.moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.sort) // Doesn't do anything because it's not hooked up, just checking the text exists
            .performClick()

        waitForThenPerformClickOn(strings.releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(
                    hasAnySibling(hasText(underPressure.date!!))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(strings.moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.showLessInfo)
            .performClick()

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.releaseGroups).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setArtist(davidBowie)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(davidBowie.name)

        composeTestRule
            .onNodeWithContentDescription(strings.filter)
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
                ArtistUi(
                    artistId = davidBowie.id,
                    sortReleaseGroupListItems = true
                )
            }
        }

        waitForThenPerformClickOn(strings.releaseGroups)
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
            .onNodeWithContentDescription(strings.moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.unsort)
            .performClick()
    }

    @Test
    fun showLessInfoInReleaseListItem() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistUi(
                    artistId = davidBowie.id,
                    showMoreInfoInReleaseListItem = false
                )
            }
        }

        waitForThenPerformClickOn(strings.releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
                useUnmergedTree = true
            )
        composeTestRule
            .onAllNodesWithText(underPressure.date!!)
            .assertCountEquals(0)
        composeTestRule
            .onNodeWithContentDescription(strings.moreActions)
            .performClick()
        composeTestRule
            .onNodeWithText(strings.showMoreInfo)
            .performClick()
    }
}
