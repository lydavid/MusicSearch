package ly.david.mbjc.ui.artist

import androidx.activity.compose.setContent
import androidx.compose.ui.test.hasNoClickAction
import androidx.compose.ui.test.hasText
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.getNameWithDisambiguation
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.network.fakeArtist
import ly.david.data.network.fakeRelease
import ly.david.data.network.fakeReleaseGroup
import ly.david.data.repository.ArtistRepository
import ly.david.mbjc.MainActivityTestWithMockServer
import ly.david.mbjc.StringReferences
import ly.david.mbjc.ui.theme.PreviewTheme
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
internal class ArtistScaffoldTest : MainActivityTestWithMockServer(), StringReferences {

    @Inject
    lateinit var artistRepository: ArtistRepository

    private fun setArtist(artistMusicBrainzModel: ArtistMusicBrainzModel) {
        composeTestRule.activity.setContent {
            PreviewTheme {
                ArtistScaffold(artistId = artistMusicBrainzModel.id)
            }
        }
    }

    @Test
    fun firstVisit_noLocalData() = runTest {
        setArtist(fakeArtist)

        assertFieldsDisplayed()
    }

    @Test
    fun repeatVisit_localData() = runTest {
        artistRepository.lookupArtist(fakeArtist.id)
        setArtist(fakeArtist)

        assertFieldsDisplayed()
    }

    private fun assertFieldsDisplayed() {
        waitForThenAssertIsDisplayed(fakeArtist.getNameWithDisambiguation())

        waitForThenAssertIsDisplayed(fakeArtist.type!!)
        waitForThenAssertIsDisplayed(fakeArtist.gender!!)

        waitForThenPerformClickOn(releaseGroups)
        waitForThenAssertIsDisplayed(fakeReleaseGroup.name)

        waitForThenPerformClickOn(releases)
        waitForThenAssertIsDisplayed(fakeRelease.name)

        waitForThenPerformClickOn(stats)
        waitForThenAssertIsDisplayed(hasText(releaseGroups).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(releases).and(hasNoClickAction()))
        waitForThenAssertIsDisplayed(hasText(relationships).and(hasNoClickAction()))
    }

    // TODO: For some reason, there's a problem with waiting for this if we put in above
    @Test
    fun hasRelations() = runTest {
        setArtist(fakeArtist)

        waitForThenPerformClickOn(relationships)
        waitForThenAssertIsDisplayed(fakeArtist.relations?.first()?.artist?.name!!)
    }
}
