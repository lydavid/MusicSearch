package ly.david.ui.common.place

import androidx.compose.ui.platform.LocalContext
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.CoordinatesUiModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CoordinateListItemTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            CoordinateListItem(
                context = LocalContext.current,
                coordinates = CoordinatesUiModel(
                    longitude = -73.98905,
                    latitude = 40.76688
                )
            )
        }
    }
}
