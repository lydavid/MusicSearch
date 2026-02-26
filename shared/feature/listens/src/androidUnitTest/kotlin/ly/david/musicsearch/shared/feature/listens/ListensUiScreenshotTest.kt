package ly.david.musicsearch.shared.feature.listens

import ly.david.musicsearch.ui.test.screenshot.ScreenshotTest
import org.junit.Test

class ListensUiScreenshotTest : ScreenshotTest() {

    @Test
    fun list() {
        snapshot {
            PreviewListensUi()
        }
    }

    @Test
    fun withRecordingFacet() {
        snapshot {
            PreviewListensUiWithRecordingFacet()
        }
    }

    @Test
    fun updated() {
        snapshot {
            PreviewListensUiUpdated()
        }
    }

    @Test
    fun needToLogin() {
        snapshot {
            PreviewListensUiNeedToLogin()
        }
    }

    @Test
    fun noUsername() {
        snapshot {
            PreviewListensUiNoUsername()
        }
    }

    @Test
    fun additionalActionsBottomSheetContent() {
        snapshot {
            PreviewListensUiListenAdditionalActionsBottomSheetContent()
        }
    }

    @Test
    fun additionalActionsBottomSheetContentAlternative() {
        snapshot {
            PreviewListensUiListenAdditionalActionsBottomSheetContentAlternative()
        }
    }

    @Test
    fun additionalActionsBottomSheetContentUnlinked() {
        snapshot {
            PreviewListensUiListenAdditionalActionsBottomSheetContentUnlinked()
        }
    }

    @Test
    fun additionalActionsBottomSheetContentFilteringByUnlinked() {
        snapshot {
            PreviewListensUiListenAdditionalActionsBottomSheetContentFilteringByUnlinked()
        }
    }

    @Test
    fun recordingFacetBottomSheetContent() {
        snapshot {
            PreviewFacetsBottomSheetContentRecordings()
        }
    }

    @Test
    fun releaseFacetBottomSheetContent() {
        snapshot {
            PreviewFacetsBottomSheetContentReleases()
        }
    }

    @Test
    fun artistFacetBottomSheetContent() {
        snapshot {
            PreviewFacetsBottomSheetContentArtists()
        }
    }
}
