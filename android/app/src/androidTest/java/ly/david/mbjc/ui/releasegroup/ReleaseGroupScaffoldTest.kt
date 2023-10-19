package ly.david.mbjc.ui.releasegroup

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import coil.Coil
import coil.ImageLoaderFactory
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.data.test.davidBowieArtistCredit
import ly.david.data.test.hotSpaceReleaseGroup
import ly.david.data.test.queenArtistCredit
import ly.david.data.test.underPressure
import ly.david.data.test.underPressureReleaseGroup
import ly.david.mbjc.MainActivityTest
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.strings.AppStrings
import ly.david.ui.core.theme.PreviewTheme
import org.junit.Before
import org.junit.Test
import org.koin.test.inject

internal class ReleaseGroupScaffoldTest : MainActivityTest() {

    private val strings: AppStrings by inject()
    private val releaseGroupRepository: ReleaseGroupRepository by inject()
    private val imageLoaderFactory: ImageLoaderFactory by inject()

    @Before
    fun setupApp() {
        Coil.setImageLoader(imageLoaderFactory)
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

        waitForThenPerformClickOn(strings.releases)
        composeTestRule
            .onNode(
                matcher = hasText(underPressure.name).and(hasAnySibling(hasText(underPressure.date!!))),
                useUnmergedTree = true
            )
            .assertIsDisplayed()

        waitForThenPerformClickOn(strings.stats)
        waitForThenAssertIsDisplayed(hasText(strings.releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(strings.relationships).and(hasNoClickAction()))
    }

    @Test
    fun hasRelations() = runTest {
        setReleaseGroup(underPressureReleaseGroup)

        waitForThenPerformClickOn(strings.relationships)
        waitForThenAssertIsDisplayed(hotSpaceReleaseGroup.name)
    }
}
