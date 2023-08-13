package ly.david.mbjc.ui.release

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import coil.Coil
import coil.ImageLoaderFactory
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.release.ReleaseRepository
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.davidBowieArtistCredit
import ly.david.data.network.elektraMusicGroup
import ly.david.data.network.fakeReleaseEvent
import ly.david.data.network.getHeader
import ly.david.data.network.queenArtistCredit
import ly.david.data.network.soulBrotherTrack
import ly.david.data.network.underPressure
import ly.david.data.network.underPressureLabelInfo
import ly.david.data.network.underPressureReleaseGroup
import ly.david.data.network.underPressureRemasterOf
import ly.david.data.network.underPressureTrack
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.common.topappbar.TopAppBarWithFilterTestTag
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.image.LargeImageTestTag
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class ReleaseScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var releaseRepository: ReleaseRepository

    @Inject
    lateinit var imageLoaderFactory: ImageLoaderFactory

    @Before
    fun setupApp() {
        hiltRule.inject()
        Coil.setImageLoader(imageLoaderFactory)
    }

    private fun setRelease(releaseMusicBrainzModel: ReleaseMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(releaseId = releaseMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest(timeout = 20.seconds) {
        setRelease(underPressure)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest(timeout = 20.seconds) {
        releaseRepository.lookupRelease(underPressure.id)
        setRelease(underPressure)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        // Title
        waitForThenAssertIsDisplayed(underPressure.name)

        testDetailsTab()
        testTracksTab()
        testRelationshipsTab()
        testStatsTab()

        // Confirm that up navigation items exists
        waitForNodeToShow(hasTestTag("TopBarSubtitle"))
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNode(
                matcher = hasText(underPressureReleaseGroup.name).and(
                    hasAnySibling(hasText(queenArtistCredit.name))
                )
            )
            .assertIsDisplayed()
        waitForThenAssertIsDisplayed(queenArtistCredit.name)
        waitForThenAssertIsDisplayed(davidBowieArtistCredit.name)
    }

    private fun testDetailsTab() {
        waitForNodeToShow(hasTestTag(LargeImageTestTag.IMAGE.name))
        composeTestRule
            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
            .performTouchInput {
                swipeUp(startY = 500f, endY = 0f)
            }
        val barcode = "Barcode: ${underPressure.barcode!!}"
        waitForThenAssertIsDisplayed(barcode)
        waitForThenAssertIsDisplayed(underPressureLabelInfo.label!!.name)
        waitForThenAssertIsDisplayed(underPressureLabelInfo.catalogNumber!!)
        waitForThenAssertIsDisplayed(elektraMusicGroup.name)
        waitForThenAssertIsDisplayed(fakeReleaseEvent.area!!.name)
        waitForThenAssertIsDisplayed(fakeReleaseEvent.date!!)
        composeTestRule
            .onNodeWithTag(LargeImageTestTag.IMAGE.name)
            .performTouchInput {
                swipeDown(startY = 0f, endY = 500f)
            }

        composeTestRule
            .onNodeWithContentDescription(filter)
            .performClick()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_TEXT_FIELD.name)
            .performTextInput("something such that we show no results")
        // TODO: test filtering the other fields
        composeTestRule
            .onNodeWithText(barcode)
            .assertIsNotDisplayedOrDoesNotExist()
        composeTestRule
            .onNodeWithTag(TopAppBarWithFilterTestTag.FILTER_BACK.name)
            .performClick()
    }

    private fun testTracksTab() {
        waitForThenPerformClickOn(tracks)
        composeTestRule
            .onNode(
                matcher = hasText(underPressureTrack.title).and(
                    hasAnySibling(hasText(underPressureTrack.number))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
        waitForThenAssertIsDisplayed(soulBrotherTrack.title)
        // TODO: attempted to test filtering but apparently our listitem nodes gets duplicated afterwards...
    }

    private fun testRelationshipsTab() {
        waitForThenPerformClickOn(relationships)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(
                    hasAnySibling(hasText("${underPressureRemasterOf.getHeader()}:"))
                ),
                useUnmergedTree = true
            )
            .assertIsDisplayed()
    }

    private fun testStatsTab() {
        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun showRetryButtonOnError() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseScaffold(
                    releaseId = "error"
                )
            }
        }

        waitForThenAssertAtLeastOneIsDisplayed(retry)

        waitForThenPerformClickOn(tracks)
        waitForThenAssertAtLeastOneIsDisplayed(retry)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertAtLeastOneIsDisplayed(retry)
    }
}
