package ly.david.ui.common.work

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.data.domain.listitem.WorkListItemModel
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class WorkListItemTest : PaparazziScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            WorkListItem(
                work = WorkListItemModel(
                    id = "1",
                    name = "work name"
                )
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            WorkListItem(
                work = WorkListItemModel(
                    id = "1",
                    name = "work name",
                    disambiguation = "that one",
                    type = "Song",
                )
            )
        }
    }
}
