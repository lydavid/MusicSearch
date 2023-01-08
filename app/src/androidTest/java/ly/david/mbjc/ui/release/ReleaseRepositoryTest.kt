package ly.david.mbjc.ui.release

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import ly.david.data.domain.AreaListItemModel
import ly.david.data.domain.ArtistCreditUiModel
import ly.david.data.domain.LabelListItemModel
import ly.david.data.domain.ReleaseGroupListItemModel
import ly.david.data.domain.ReleaseScaffoldModel
import ly.david.data.network.CoverArtArchive
import ly.david.data.network.fakeRelease
import ly.david.data.repository.ReleaseRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@OptIn(ExperimentalCoroutinesApi::class)
class ReleaseRepositoryTest {

    @get:Rule(order = 0)
    val hiltRule: HiltAndroidRule by lazy { HiltAndroidRule(this) }

    @Inject
    lateinit var releaseRepository: ReleaseRepository

    @Before
    fun setupApp() {
        hiltRule.inject()
    }

    @Test
    fun lookupRelease() = runTest {
        assertEquals(
            ReleaseScaffoldModel(
                id = "fakeRelease1",
                name = "Release Name",
                disambiguation = "",
                date = null,
                barcode = null,
                status = null,
                statusId = null,
                countryCode = null,
                packaging = null,
                packagingId = null,
                asin = null,
                quality = null,
                coverArtArchive = CoverArtArchive(count = 0),
                textRepresentation = null,
                coverArtPath = null,
                formattedFormats = "CD",
                formattedTracks = "2",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "artist1",
                        name = "Different Artist Name",
                        joinPhrase = " feat. "
                    ), ArtistCreditUiModel(artistId = "artist2", name = "Other Artist", joinPhrase = null)
                ),
                releaseGroup = ReleaseGroupListItemModel(
                    id = "fakeReleaseGroup1",
                    name = "Release Group Name",
                    firstReleaseDate = "",
                    disambiguation = "",
                    primaryType = null,
                    secondaryTypes = listOf(),
                    artistCredits = listOf(),
                    hasCoverArt = null,
                    coverArtUrl = null
                ),
                areas = listOf(
                    AreaListItemModel(
                        id = "area2",
                        name = "Country Name",
                        disambiguation = null,
                        type = "Country",
                        lifeSpan = null,
                        iso_3166_1_codes = listOf(),
                        date = "2022-10-29"
                    )
                ),
                labels = listOf(
                    LabelListItemModel(
                        id = "label2",
                        name = "Label Name 2",
                        disambiguation = null,
                        type = "Production",
                        labelCode = 321,
                        catalogNumber = ""
                    ),
                    LabelListItemModel(
                        id = "label1",
                        name = "Label Name 1",
                        disambiguation = null,
                        type = "Imprint",
                        labelCode = 123,
                        catalogNumber = "CAT 1"
                    )
                ),
                releaseLength = 25301000,
                hasNullLength = false
            ),
            releaseRepository.lookupRelease(fakeRelease.id)
        )
    }
}
