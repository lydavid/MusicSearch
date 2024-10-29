package ly.david.musicsearch.ui.common.area

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class AreaListItemTest : ScreenshotTest() {
    @Test
    fun areaListItem() {
        snapshot {
            PreviewAreaListItem()
        }
    }

    @Test
    fun areaListItemDisambiguation() {
        snapshot {
            PreviewAreaListItemDisambiguation()
        }
    }

    @Test
    fun areaListItemCountry() {
        snapshot {
            PreviewAreaListItemCountry()
        }
    }

    @Test
    fun areaListItemWorldwide() {
        snapshot {
            PreviewAreaListItemWorldwide()
        }
    }

    @Test
    fun releaseEvent() {
        snapshot {
            PreviewReleaseEvent()
        }
    }

    @Test
    fun visited() {
        snapshot {
            PreviewAreaListItemVisited()
        }
    }

    @Test
    fun releaseEventVisited() {
        snapshot {
            PreviewReleaseEventVisited()
        }
    }
}
