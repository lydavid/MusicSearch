package ly.david.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.ui.common.PaparazziScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(TestParameterInjector::class)
class TopAppBarWithFilterTest : PaparazziScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            TopAppBarWithFilterInternal(title = "Title")
        }
    }

    @Test
    fun filterMode() {
        snapshot {
            TopAppBarWithFilterInternal(
                title = "Title",
                isFilterMode = true
            )
        }
    }

    @Test
    fun noFilter() {
        snapshot {
            TopAppBarWithFilterInternal(
                title = "Title",
                showFilterIcon = false
            )
        }
    }

    @Test
    fun withTabs() {
        snapshot {
            TopAppBarWithFilterInternal(
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                tabsTitles = listOf("Tab 1", "Tab 2", "Tab 3"),
                selectedTabIndex = 1
            )
        }
    }
}
