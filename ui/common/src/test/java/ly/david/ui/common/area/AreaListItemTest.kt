package ly.david.ui.common.area

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.core.models.area.AreaType
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class AreaListItemTest : ScreenshotTest() {

    @Test
    fun simple() {
        snapshot {
            AreaListItem(
                area = AreaListItemModel(
                    id = "1",
                    name = "Area Name",
                ),
            )
        }
    }

    @Test
    fun allInfo() {
        snapshot {
            AreaListItem(
                area = AreaListItemModel(
                    id = "3",
                    name = "Area Name with a very long name",
                    disambiguation = "That one",
                    type = AreaType.COUNTRY,
                    countryCodes = listOf("GB"),
                ),
            )
        }
    }

    @Test
    fun releaseEvent() {
        snapshot {
            PreviewReleaseEvent()
        }
    }
}
