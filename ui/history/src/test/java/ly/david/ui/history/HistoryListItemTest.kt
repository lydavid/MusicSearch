package ly.david.ui.history

import coil.Coil
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.data.image.FakeImageLoader
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
class HistoryListItemTest : PaparazziScreenshotTest() {

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
    fun releaseGroup() {
        snapshot {
            PreviewLookupHistoryReleaseGroup(imageUrl = "")
        }
    }

    @Test
    fun release() {
        snapshot {
            PreviewLookupHistoryRelease(imageUrl = "")
        }
    }

    @Test
    fun artist() {
        snapshot {
            PreviewLookupHistoryArtist(imageUrl = "")
        }
    }

    @Test
    fun releaseGroupWithCoverArt() {
        snapshot {
            PreviewLookupHistoryReleaseGroup()
        }
    }

    @Test
    fun releaseWithCoverArt() {
        snapshot {
            PreviewLookupHistoryRelease()
        }
    }

    @Test
    fun artistWithCoverArt() {
        snapshot {
            PreviewLookupHistoryArtist()
        }
    }
}
