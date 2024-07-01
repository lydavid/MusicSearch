package ly.david.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(TestParameterInjector::class)
class TopAppBarWithFilterTest : ScreenshotTest() {

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
                isFilterMode = true,
            )
        }
    }

    @Test
    fun noFilter() {
        snapshot {
            TopAppBarWithFilterInternal(
                title = "Title",
                showFilterIcon = false,
            )
        }
    }

    @Test
    fun withTabs() {
        snapshot {
            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            TopAppBarWithFilterInternal(
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                additionalBar = {
                    TabsBar(
                        tabsTitle = listOf("Tab 1", "Tab 2", "Tab 3"),
                        selectedTabIndex = selectedTabIndex,
                        onSelectTabIndex = { selectedTabIndex = it },
                    )
                },
            )
        }
    }
}
