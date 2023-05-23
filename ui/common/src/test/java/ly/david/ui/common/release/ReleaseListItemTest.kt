package ly.david.ui.common.release

import androidx.compose.material3.Surface
import app.cash.paparazzi.DeviceConfig
import coil.Coil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.data.domain.ReleaseListItemModel
import ly.david.data.persistence.area.ReleaseCountry
import ly.david.ui.common.FakeImageLoader
import ly.david.ui.common.PaparazziScreenshotTest
import ly.david.ui.common.theme.PreviewTheme
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class ReleaseListItemTest(
    config: DeviceConfig
) : PaparazziScreenshotTest(config) {

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
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    ReleaseListItem(
                        release = ReleaseListItemModel(
                            id = "1",
                            name = "Release title",
                        ),
                        showMoreInfo = true
                    )
                }
            }
        }
    }

    @Test
    fun withDisambiguation() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
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
        }
    }

    @Test
    fun allInfo() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
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
        }
    }

    @Test
    fun allInfoButShowLessInfo() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
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
        }
    }

    @Test
    fun multipleReleaseEvents() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
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
        }
    }

    @Test
    fun withCoverArt() {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    ReleaseListItem(
                        release = ReleaseListItemModel(
                            id = "1",
                            name = "Release title",
                            coverArtPath = "https://www.example.com/image.jpg"
                        ),
                        showMoreInfo = true
                    )
                }
            }
        }
    }
}
