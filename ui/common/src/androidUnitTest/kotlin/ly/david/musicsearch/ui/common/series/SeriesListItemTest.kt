package ly.david.musicsearch.ui.common.series

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SeriesListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                ),
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                    disambiguation = "that one",
                    type = "Tour",
                ),
            )
        }
    }
}
