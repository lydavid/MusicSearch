package ly.david.musicsearch.ui.common.work

import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

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
