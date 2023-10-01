package ly.david.ui.common.release

import coil.Coil
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.data.core.release.ReleaseCountry
import ly.david.data.domain.listitem.ReleaseListItemModel
import ly.david.ui.test.image.FakeImageLoader
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
class ReleaseListItemTest : PaparazziScreenshotTest() {

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
    fun titleOnly() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                ),
                showMoreInfo = true
            )
        }
    }

    @Test
    fun withDisambiguation() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    disambiguation = "Disambiguation text",
                ),
                showMoreInfo = true
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    disambiguation = "Disambiguation text",
                    date = "2021-09-08",
                    countryCode = "CA",
                    formattedFormats = "2×CD + Blu-ray",
                    formattedTracks = "15 + 8 + 24"
                ),
                showMoreInfo = true
            )
        }
    }

    @Test
    fun allInfoButShowLessInfo() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    disambiguation = "Disambiguation text",
                    date = "2021-09-08",
                    countryCode = "CA",
                    formattedFormats = "2×CD + Blu-ray",
                    formattedTracks = "15 + 8 + 24"
                ),
                showMoreInfo = false
            )
        }
    }

    @Test
    fun multipleReleaseEvents() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    countryCode = "CA",
                    releaseCountries = listOf(
                        ReleaseCountry("1", countryId = "2"),
                        ReleaseCountry("1", countryId = "3"),
                        ReleaseCountry("1", countryId = "4"),
                    ),
                ),
                showMoreInfo = true
            )
        }
    }

    @Test
    fun withCoverArt() {
        snapshot {
            ReleaseListItem(
                release = ReleaseListItemModel(
                    id = "1",
                    name = "Release title",
                    imageUrl = "https://www.example.com/image.jpg"
                ),
                showMoreInfo = true
            )
        }
    }
}
