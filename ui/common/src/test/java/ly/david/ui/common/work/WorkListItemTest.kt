package ly.david.ui.common.work

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.data.core.listitem.WorkListItemModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class WorkListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            WorkListItem(
                work = WorkListItemModel(
                    id = "1",
                    name = "work name",
                ),
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
                ),
            )
        }
    }
}
