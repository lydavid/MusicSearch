package ly.david.ui.common.place

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.core.models.place.CoordinatesUiModel
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class CoordinateListItemTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            CoordinateListItem(
                coordinates = CoordinatesUiModel(
                    longitude = -73.98905,
                    latitude = 40.76688,
                ),
            )
        }
    }
}
