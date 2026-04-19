package ly.david.musicsearch.shared.feature.search.url

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class LookupUrlUiScreenshotTest : ScreenshotTest(isFullScreen = true) {

    @Test
    fun default() {
        snapshot {
            PreviewLookupUrlUi()
        }
    }

    @Test
    fun loading() {
        snapshot {
            PreviewLookupUrlUiLoading()
        }
    }

    @Test
    fun singleResult() {
        snapshot {
            PreviewLookupUrlUiSingleResult()
        }
    }

    @Test
    fun multipleResults() {
        snapshot {
            PreviewLookupUrlUiMultipleResults()
        }
    }

    @Test
    fun cannotBeEmpty() {
        snapshot {
            PreviewLookupUrlUiCannotBeEmpty()
        }
    }

    @Test
    fun notFound() {
        snapshot {
            PreviewLookupUrlUiNotFound()
        }
    }

    @Test
    fun badRequest() {
        snapshot {
            PreviewLookupUrlUiBadRequest()
        }
    }

    @Test
    fun otherError() {
        snapshot {
            PreviewLookupUrlUiOtherError()
        }
    }
}
