package ly.david.ui.common.instrument

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.domain.listitem.InstrumentListItemModel
import ly.david.ui.test.screenshot.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class InstrumentListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            InstrumentListItem(
                instrument = InstrumentListItemModel(
                    id = "1",
                    name = "Piano",
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            InstrumentListItem(
                instrument = InstrumentListItemModel(
                    id = "4",
                    name = "baroque guitar",
                    disambiguation = "Baroque gut string guitar",
                    type = "String instrument",
                    description = "Predecessor of the modern classical guitar, it had gut strings and even gut frets." +
                        " First described in 1555, it surpassed the Renaissance lute's popularity."
                )
            )
        }
    }
}
