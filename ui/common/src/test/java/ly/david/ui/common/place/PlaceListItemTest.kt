package ly.david.ui.common.place

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.LifeSpanUiModel
import ly.david.data.domain.listitem.PlaceListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class PlaceListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            PlaceListItem(
                place = PlaceListItemModel(
                    id = "2",
                    name = "Place Name",
                    address = "123 Fake St"
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            PlaceListItem(
                place = PlaceListItemModel(
                    id = "ed121457-87f6-4df9-a24b-d3f1bab1fdad",
                    name = "Sony Music Studios",
                    disambiguation = "NYC, closed 2007",
                    type = "Studio",
                    address = "460 W. 54th St., at 10th Avenue, Manhatten, NY",
                    lifeSpan = LifeSpanUiModel(begin = "1993", end = "2007-08", ended = true)
                )
            )
        }
    }
}
