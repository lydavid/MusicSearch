package ly.david.ui.common.series

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.domain.listitem.SeriesListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class SeriesListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            SeriesListItem(
                series = SeriesListItemModel(
                    id = "1",
                    name = "series name",
                )
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
                )
            )
        }
    }
}
