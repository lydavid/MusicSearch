package ly.david.ui.common.release

import coil.Coil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.ui.test.image.FakeImageLoader
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReleaseListItemTest : ScreenshotTest() {

    @Before
    fun before() {
        val fakeImageLoader = FakeImageLoader()
        Coil.setImageLoader(fakeImageLoader)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun releaseListItem() {
        snapshot {
            PreviewReleaseListItem()
        }
    }

    @Test
    fun releaseListItemDisambiguation() {
        snapshot {
            PreviewReleaseListItemDisambiguation()
        }
    }

    @Test
    fun releaseListItemDisambiguationCountry() {
        snapshot {
            PreviewReleaseListItemDisambiguationCountry()
        }
    }

    @Test
    fun releaseListItemCountry() {
        snapshot {
            PreviewReleaseListItemCountry()
        }
    }

    @Test
    fun releaseListItemDateCountryFormatsTracks() {
        snapshot {
            PreviewReleaseListItemDateCountryFormatsTracks()
        }
    }

    @Test
    fun releaseListItemCountryDate() {
        snapshot {
            PreviewReleaseListItemCountryDate()
        }
    }

    @Test
    fun releaseListItemDateArtistCredits() {
        snapshot {
            PreviewReleaseListItemDateArtistCredits()
        }
    }

    @Test
    fun releaseListItemMultipleCountries() {
        snapshot {
            PreviewReleaseListItemMultipleCountries()
        }
    }

    @Test
    fun releaseListItemShowLessInfo() {
        snapshot {
            PreviewReleaseListItemShowLessInfo()
        }
    }

    @Test
    fun releaseListItemCatalog() {
        snapshot {
            PreviewReleaseListItemCatalog()
        }
    }

    @Test
    fun withCoverArt() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    imageUrl = "https://www.example.com/image.jpg",
                ),
                showMoreInfo = true,
            )
        }
    }
}
