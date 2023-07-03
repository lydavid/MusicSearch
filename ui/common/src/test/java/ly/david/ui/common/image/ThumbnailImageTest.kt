package ly.david.ui.common.image

import coil.Coil
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.data.image.FakeImageLoader
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

// TODO: seems to fill vertically as well
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
class ThumbnailImageTest : PaparazziScreenshotTest() {

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
    fun default() {
        snapshot {
            PreviewThumbnailImage()
        }
    }
}
