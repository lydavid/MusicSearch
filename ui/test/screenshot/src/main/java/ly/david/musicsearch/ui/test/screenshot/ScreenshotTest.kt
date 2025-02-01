package ly.david.musicsearch.ui.test.screenshot

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.cash.paparazzi.detectEnvironment
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import com.android.ide.common.rendering.api.SessionParams
import com.android.resources.NightMode
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import ly.david.musicsearch.test.image.getFakeImageLoader
import ly.david.musicsearch.ui.core.theme.PreviewTheme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

private const val PAPARAZZI_THEME = "android:Theme.Material.Light.NoActionBar"

/**
 * Common setup for running screenshot tests on light and dark mode.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(TestParameterInjector::class)
abstract class ScreenshotTest(
    private val isFullScreen: Boolean = false,
) {
    @TestParameter
    private lateinit var nightMode: NightMode

    // Note we cannot override junit Rule
    @get:Rule
    val paparazzi: Paparazzi by lazy {
        Paparazzi(
            environment = detectEnvironment().run {
                copy(
                    compileSdkVersion = 33,
                    platformDir = platformDir.replace(
                        "35",
                        "33",
                    ),
                )
            },
            deviceConfig = DeviceConfig.PIXEL_5.copy(nightMode = nightMode),
            theme = PAPARAZZI_THEME,
            renderingMode = if (isFullScreen) {
                SessionParams.RenderingMode.NORMAL
            } else {
                SessionParams.RenderingMode.SHRINK
            },
            showSystemUi = false,
        )
    }

    protected fun snapshot(content: @Composable () -> Unit) {
        paparazzi.snapshot {
            PreviewTheme {
                Surface {
                    content()
                }
            }
        }
    }

    @OptIn(DelicateCoilApi::class)
    @Before
    fun before() {
        SingletonImageLoader.setUnsafe(getFakeImageLoader(paparazzi.context))
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}
