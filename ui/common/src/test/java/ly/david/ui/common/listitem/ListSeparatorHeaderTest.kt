package ly.david.ui.common.listitem

import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import ly.david.ui.common.R
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class ListSeparatorHeaderTest : PaparazziScreenshotTest() {

    @Test
    fun listSeparatorHeader() {
        snapshot {
            ListSeparatorHeader("Album + Compilation")
        }
    }

    @Test
    fun attributesListSeparatorHeader() {
        snapshot {
            AttributesListSeparatorHeader(R.string.work)

        }
    }

    @Test
    fun informationListSeparatorHeader() {
        snapshot {
            InformationListSeparatorHeader(R.string.area)
        }
    }
}
