package ly.david.mbjc.ui.release

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import coil.Coil
import coil.ImageLoaderFactory
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.ReleaseMusicBrainzModel
import ly.david.data.network.fakeArtistCredit
import ly.david.data.network.fakeArtistCredit2
import ly.david.data.network.fakeLabel2
import ly.david.data.network.fakeLabelInfo
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseEvent
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.network.fakeTrack
import ly.david.data.repository.ReleaseRepository
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
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
    fun firstVisit_noLocalData() = runTest {
        setRelease(fakeRelease)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        releaseRepository.lookupRelease(fakeRelease.id)
        setRelease(fakeRelease)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {

        waitForThenAssertIsDisplayed(fakeRelease.name)
        waitForThenAssertIsDisplayed(fakeLabelInfo.label!!.name)
        composeTestRule
            .onNodeWithText(fakeLabelInfo.catalogNumber!!)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeLabel2.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeReleaseEvent.area!!.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeReleaseEvent.date!!)
            .assertIsDisplayed()

        composeTestRule.waitUntil(10_000L) {
            composeTestRule
                .onAllNodes(hasTestTag("coverArtImage"))
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule
            .onNodeWithTag("coverArtImage")
            .assertExists() // assertIsDisplayed fails but it does exist

        // TODO: maybe don't test like this, it's hard to reference their values
        waitForThenPerformClickOn(tracks)
        waitForThenAssertIsDisplayed(fakeRelease.media!!.first().tracks!!.first().title)
        waitForThenAssertIsDisplayed(fakeRelease.media!!.first().tracks!!.last().title)
        waitForThenAssertIsDisplayed(fakeTrack.title)
        // TODO: attempted to test filtering but apparently our listitem nodes gets duplicated afterwards...

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeRelease.relations?.first()?.release?.name!!)

        // TODO: no tracks stats
        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(fakeReleaseGroup.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit2.name)
            .assertIsDisplayed()
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

        composeTestRule
            .onNodeWithText(tracks)
            .performClick()

        waitForThenAssertAtLeastOneIsDisplayed(retry)
    }
}
