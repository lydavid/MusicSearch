package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.fakeArtistCredit
import ly.david.data.network.fakeArtistCredit2
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.repository.ReleaseGroupRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ReleaseGroupScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var releaseGroupRepository: ReleaseGroupRepository

    private fun setReleaseGroup(releaseGroupMusicBrainzModel: ReleaseGroupMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(releaseGroupId = releaseGroupMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setReleaseGroup(fakeReleaseGroup)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        releaseGroupRepository.lookupReleaseGroup(fakeReleaseGroup.id)
        setReleaseGroup(fakeReleaseGroup)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeReleaseGroup.name)

        // Confirm that up navigation items exists
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()
        composeTestRule
            .onNodeWithText(fakeArtistCredit.name)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText(fakeArtistCredit2.name)
            .assertIsDisplayed()

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(fakeRelease.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setReleaseGroup(fakeReleaseGroup)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeReleaseGroup.relations?.first()?.artist?.name!!)
    }


    @Test
    fun showRetryButtonOnError() = runTest {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(
                    releaseGroupId = "error"
                )
            }
        }

        waitForThenAssertIsDisplayed(retry)

        composeTestRule
            .onNodeWithText(relationships)
            .performClick()

        waitForThenAssertIsDisplayed(retry)
    }
}
