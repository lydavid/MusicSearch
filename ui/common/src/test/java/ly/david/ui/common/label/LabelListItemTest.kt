package ly.david.ui.common.label

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class LabelListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            LabelListItem(
                label = LabelListItemModel(
                    id = "1",
                    name = "Music Label",
                ),
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            LabelListItem(
                label = LabelListItemModel(
                    id = "5",
                    name = "Sony Music",
                    disambiguation = "global brand, excluding JP, owned by Sony Music Entertainment",
                    type = "Original Production",
                    labelCode = 10746,
                    catalogNumber = "CAT-123",
                ),
            )
        }
    }
}
