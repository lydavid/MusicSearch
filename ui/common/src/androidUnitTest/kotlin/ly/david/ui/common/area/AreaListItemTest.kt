package ly.david.ui.common.area

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterValuesProvider
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AreaListItemTest : ScreenshotTest() {

    @Test
    fun areaListItem(
        @TestParameter(valuesProvider = AreasProvider::class) area: AreaListItemModel,
    ) {
        snapshot {
            PreviewAreaListItem(area = area)
        }
    }

    @Test
    fun releaseEvent() {
        snapshot {
            PreviewReleaseEvent()
        }
    }

    class AreasProvider : TestParameterValuesProvider() {
        override fun provideValues(context: Context?): List<AreaListItemModel> = areas
    }
}
