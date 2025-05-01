package ly.david.musicsearch.ui.common.relation

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class UrlListItemTest : ScreenshotTest() {

    @Test
    fun url() {
        snapshot {
            PreviewUrlListItem()
        }
    }

    @Test
    fun wikipedia() {
        snapshot {
            PreviewUrlListItemWikipedia()
        }
    }

    @Test
    fun wikidata() {
        snapshot {
            PreviewUrlListItemWikidata()
        }
    }
}
