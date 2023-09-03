package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.releasegroup.ReleaseGroupRepository
import ly.david.data.musicbrainz.ReleaseGroupMusicBrainzModel
import ly.david.data.test.davidBowieArtistCredit
import ly.david.data.test.hotSpaceReleaseGroup
import ly.david.data.test.queenArtistCredit
import ly.david.data.test.underPressure
import ly.david.data.test.underPressureReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.mbjc.StringReferences
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test

@HiltAndroidTest
internal class ReleaseGroupScaffoldTest : MainActivityTest(), StringReferences {

    @Inject
    lateinit var releaseGroupRepository: ReleaseGroupRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    private fun setReleaseGroup(releaseGroupMusicBrainzModel: ReleaseGroupMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ReleaseGroupScaffold(releaseGroupId = releaseGroupMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setReleaseGroup(underPressureReleaseGroup)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        releaseGroupRepository.lookupReleaseGroup(underPressureReleaseGroup.id)
        setReleaseGroup(underPressureReleaseGroup)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(underPressureReleaseGroup.name)

        // Confirm that up navigation items exists
        waitForNodeToShow(hasTestTag("TopBarSubtitle"))
        composeTestRule
            .onNodeWithTag("TopBarSubtitle")
            .performClick()

        waitForThenAssertIsDisplayed(davidBowieArtistCredit.name)
        waitForThenAssertIsDisplayed(queenArtistCredit.name)

        waitForThenPerformClickOn(releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
                useUnmergedTree = true
            )
            .assertIsDisplayed()

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setReleaseGroup(underPressureReleaseGroup)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(hotSpaceReleaseGroup.name)
    }
}
