package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(TestParameterInjector::class)
class ScrollableTopAppBarTest : ScreenshotTest() {

    @Test
    fun default() {
        snapshot {
            ScrollableTopAppBar(
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                subtitle = "A subtitle that is also very long that will also go off the screen.",
            )
        }
    }

    @Test
    fun withIcon() {
        snapshot {
            ScrollableTopAppBar(
                entity = MusicBrainzEntity.ARTIST,
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                subtitle = "A subtitle that is also very long that will also go off the screen.",
            )
        }
    }

    @Test
    fun withTabs() {
        snapshot {
            var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

            ScrollableTopAppBar(
                entity = MusicBrainzEntity.RELEASE_GROUP,
                title = "A title that is very long so that it will go off the screen and allow us to scroll.",
                subtitle = "A subtitle that is also very long that will also go off the screen.",
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
